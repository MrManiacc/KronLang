import com.kron.parse.Parser
import com.kron.source.SourceData
import com.kron.token.Lexer

fun main() {
    val source = SourceData.fromResource("test1.kron").source
    val lexer = Lexer.of { source }
    lexer.tokenize()
    println(lexer.tokens)
    val parser = Parser(lexer)
    parser.parse()
}