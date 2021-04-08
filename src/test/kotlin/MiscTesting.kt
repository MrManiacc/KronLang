import com.kron.modifiers.Modifier.*
import com.kron.parse.Parser
import com.kron.source.SourceData
import com.kron.token.Lexer
import org.junit.jupiter.api.Test

class MiscTesting {

    @Test
    fun `test modifiers for the NodeModifiers enum`() {
        //This would be like having a [public static final] modifiers
        val mods = PublicAccess with GlobalScope with ModuleScope build FileScope
        println(mods)

    }

    @Test
    fun `testing the parser's variable paring stuff`() {
        val source = SourceData.fromResource("test1.kron").source
        val lexer = Lexer.of { source }
        lexer.tokenize()
        val parser = Parser(lexer)
        parser.parse()
    }
}