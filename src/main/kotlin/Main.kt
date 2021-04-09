import com.kron.dsl.*
import com.kron.dsl.visit
import com.kron.parse.Parser
import com.kron.parse.structure.rule.*
import com.kron.parse.structure.visitor.Visitors
import com.kron.source.SourceData
import com.kron.token.Lexer

fun main(vararg args: String) {
    var testFIle = "test.kron"
    if (args.isNotEmpty()) testFIle = args[0]
    val ruleSet = RuleSet.new() name testFIle
    var parseType: RuleType = RuleType.StatementList
    if (args.size >= 2) {
        parseType = enumValueOf(args[1])
        for (i in 1..args.lastIndex) {
            ruleSet with enumValueOf(args[i])
        }
    } else ruleSet name "default" with RuleType.Program with RuleType.Compound with RuleType.Statement with RuleType.StatementList with RuleType.Assignment with RuleType.Expression with RuleType.Term
    val source = SourceData.fromResource(testFIle).source
    val lexer = Lexer.of { source }.apply { tokenize { it.isEmpty } }
    val parser = Parser(
        lexer, ruleSet
    )
    println("lexer result:${lexer.tokensString}")
    val expression = parser.parse(parseType)
    println("expression result:\n\t$expression")
    val output = Visitors.visit(expression)
    println("visited result:\n\t$output")
}