package live.bunch.interceptor

import io.micronaut.aop.InterceptedMethod
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import io.micronaut.aop.kotlin.KotlinInterceptedMethod
import io.micronaut.core.util.KotlinUtils
import jakarta.inject.Singleton
import java.util.concurrent.CompletionException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resumeWithException


@Singleton
@InterceptorBean(FaultyInterceptorAnnotation::class)
class FaultyInterceptor() : MethodInterceptor<Any?, Any?> {
//    override fun intercept(context: MethodInvocationContext<Any?, Any?>): Any? {
//        val executorService = Executors.newSingleThreadExecutor()
//
//        if (context.isSuspend) {
//            val interceptedMethod = InterceptedMethod.of(context)
//            println("Attempting to intercept method ${context.methodName}")
//
//            try {
//                val res = interceptedMethod.handleResult(
//                    CompletableFuture.supplyAsync(
//                        { interceptedMethod.interceptResultAsCompletionStage() },
//                        executorService
//                    )
//                )
//                println("Proceeded with context and got result $res")
//                return res
//            } catch (ex: Throwable) {
//                println("EXPECTED PRINT - Encountered exception attempting on context.proceed()")
//                ex.printStackTrace()
//                throw ex
//            }
//
//        } else {
//            println("Method ${context.methodName} is not a suspend function")
//            return context.proceed()
//        }
//    }
    override fun intercept(context: MethodInvocationContext<Any?, Any?>): Any? {
        if (context.isSuspend) {
            println("Attempting to intercept method ${context.methodName}")

            try {
                val res = handleResult(context)
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

    private fun handleResult(context: MethodInvocationContext<Any?, Any?>): Any? {
        val interceptedMethod = InterceptedMethod.of(context) as KotlinInterceptedMethod
        val result = interceptedMethod.interceptResultAsCompletionStage()

//        val parameterValues: Array<Any> = context.parameterValues
//        val lastParameterIndex = parameterValues.size - 1
//        val lastArgumentValue = parameterValues[lastParameterIndex]
//        val continuation = lastArgumentValue as Continuation<Any?>
        val continuation = context.parameterValues.last() as Continuation<Any?>

        result.whenComplete { value: Any?, throwable: Throwable? ->
            if (throwable == null) {
                val success = Result.success(value)
                println("JACOB - Method returned $value")
                continuation.resumeWith(success)
            } else {
                val exception = if (throwable is CompletionException) throwable.cause ?: throwable else throwable
                println("JACOB - Caught by interceptor: ${exception.message}")
                continuation.resumeWithException(exception)
            }
        }
        return KotlinUtils.COROUTINE_SUSPENDED
    }


    private fun handleResultBad(
        context: MethodInvocationContext<Any?, Any?>
    ): Any? {
        val interceptedMethod = InterceptedMethod.of(context) as KotlinInterceptedMethod
        val result = interceptedMethod.interceptResultAsCompletionStage()

//        val parameterValues: Array<Any> = context.parameterValues
//        val lastParameterIndex = parameterValues.size - 1
//        val lastArgumentValue = parameterValues[lastParameterIndex]
//        val continuation = lastArgumentValue as Continuation<Any?>
        val continuation = context.parameterValues.last() as Continuation<Any?>

        return result.whenComplete { value: Any?, throwable: Throwable? ->
            if (throwable == null) {
                val success = Result.success(value)
                println("JACOB - Method returned $value")
                continuation.resumeWith(success)
            } else {
                val exception = if (throwable is CompletionException) throwable.cause ?: throwable else throwable
                println("JACOB - Caught by interceptor: ${exception.message}")
                continuation.resumeWithException(exception)
            }
        }
    }
}
