import com.kron.token.Lexer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LexerTester {
    val source = """
      
    """

    var lexer: Lexer? = null

    @Test
    fun `the total number of tokens should be `() {
        lexer = Lexer.of { "" }
    }

}