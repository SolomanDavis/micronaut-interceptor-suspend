package live.bunch

import jakarta.inject.Singleton
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import live.bunch.interceptor.FaultyInterceptorAnnotation

@Singleton
@FaultyInterceptorAnnotation
class ExampleImpl {
    suspend fun suspendFunc(): Boolean = coroutineScope {
        val deferred = async {
            delay(500)
            return@async "OK"
        }
        val result = deferred.await()
        println("printing result so kotlin doesn't optimize deferred - $result")
        throw RuntimeException("RUNTIME EXCEPTION THAT SHOULD BE THROWN AND CAUGHT")
    }
}
