package live.bunch
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows

@MicronautTest
class InterceptorSuspendTest(
    private val exampleImpl: ExampleImpl
) {
    @Test
    fun testItWorksException() = runBlocking {
        val ex: RuntimeException = assertThrows { exampleImpl.suspendFunc(false) }

        println("DONE TEST - ${ex.message}")
    }
    @Test
    fun testItWorksExceptionNotCancelled() = runBlocking {
        val ex: RuntimeException = assertThrows {
            val x = exampleImpl.suspendFunc(false)
            println(x)
        }

        println("DONE TEST - ${ex.message}")
    }
    @Test
    fun testItWorksReturning() = runBlocking {
        assertTrue(exampleImpl.suspendFunc(true))

        println("DONE TEST - PASSED")
    }
}
