package live.bunch
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertThrows

@MicronautTest
class InterceptorSuspendTest(
    private val exampleImpl: ExampleImpl
) {
    @Test
    fun testItWorks() = runBlocking {
        val ex: RuntimeException = assertThrows { exampleImpl.suspendFunc() }

        println("DONE TEST - ${ex.message}")
    }
}
