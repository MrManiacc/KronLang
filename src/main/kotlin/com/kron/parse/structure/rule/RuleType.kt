package com.kron.parse.structure.rule

import com.kron.parse.structure.rule.grammer.Expression as ExpressionGrammar

/**
 * Stores the different rule types. These can be executed upon and chained.
 */
enum class RuleType(val rule: IRule) {
    Program(Grammar::program),
    Compound(Grammar::compound),
    StatementList(Grammar::statementList),
    Statement(Grammar::statement),
    Assignment(Grammar::assignment),
    NoOperation(Grammar::noOperation),
    Expression(ExpressionGrammar::expression),
    Term(ExpressionGrammar::term),
    Factor(ExpressionGrammar::factor),
    Variable(Grammar::variable),
    Modifier(Grammar::modifiers),
}