import com.kron.dsl.*
import com.kron.dsl.visit
import com.kron.parse.Parser
import com.kron.parse.structure.rule.*
import com.kron.parse.structure.visitor.Visitors
import com.kron.source.SourceData
import com.kron.token.Lexer

fun main() {
    val source = SourceData.fromResource("test1.kron").source
    val lexer = Lexer.of { source }.apply { tokenize { it.isEmpty } }
    val parser = Parser(lexer)
    println("lexer result:${lexer.tokensString}")
    val expression = parser.parse(RuleType.StatementList)
    println("expression result:\n\t$expression")
    val output = Visitors.visit(expression)
    println("visited result:\n\t$output")
}