import com.kron.dsl.visit
import com.kron.parse.Parser
import com.kron.parse.structure.visitor.IVisitor
import com.kron.parse.structure.visitor.visitors.*
import com.kron.parse.structure.visitor.visitors.Visitors
import com.kron.source.SourceData
import com.kron.token.Lexer

fun main() {
    val source = SourceData.fromResource("test1.kron").source
    val lexer = Lexer.of { source }
    lexer.tokenize()
    //    println(lexer.tokens.filter { !it.isNone && it.isValid }.toList())
    //    println("==========================================================")
    val parser = Parser(lexer)
    val result = parser.parse()
    println(result)
    val output = Visitors.visit(result)
    println(output)
}