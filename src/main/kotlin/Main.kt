import com.kron.source.SourceData
import com.kron.token.Lexer

fun main() {
    val source = SourceData.fromResource("test0.kron").source
//    val source = SourceData.fromResource("test0.kron").source
    val lexer = Lexer.of { source }
//    println(source)
    lexer.tokenize()
    println(lexer)
}