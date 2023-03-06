package live.bunch.interceptor

import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import jakarta.inject.Singleton

@Singleton
@InterceptorBean(FaultyInterceptorAnnotation::class)
class FaultyInterceptor() : MethodInterceptor<Any?, Any?> {
    override fun intercept(context: MethodInvocationContext<Any?, Any?>): Any? {
        if (context.isSuspend) {
            println("Attempting to intercept method ${context.methodName}")

            try {
                val res = context.proceed()
                println("Proceeded with context and got result $res")
                return res
            } catch (ex: Throwable) {
                println("EXPECTED PRINT - Encountered exception attempting on context.proceed()")
                ex.printStackTrace()
                throw ex
            }

        } else {
            println("Method ${context.methodName} is not a suspend function")
            return context.proceed()
        }
    }
}
