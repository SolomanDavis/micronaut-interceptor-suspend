package live.bunch.interceptor

import io.micronaut.aop.InterceptedMethod
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import jakarta.inject.Singleton
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

@Singleton
@InterceptorBean(FaultyInterceptorAnnotation::class)
class FaultyInterceptor() : MethodInterceptor<Any?, Any?> {
    override fun intercept(context: MethodInvocationContext<Any?, Any?>): Any? {
        val executorService = Executors.newSingleThreadExecutor()

        if (context.isSuspend) {
            val interceptedMethod = InterceptedMethod.of(context)
            println("Attempting to intercept method ${context.methodName}")

            try {
                val res = interceptedMethod.handleResult(
                    CompletableFuture.supplyAsync(
                        { interceptedMethod.interceptResultAsCompletionStage() },
                        executorService
                    )
                )
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
