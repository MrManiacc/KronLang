package com.kron.parse.structure.rule

/**
 * Stores the different rule types. These can be executed upon and chained.
 */
enum class RuleType(val rule: IRule) {
    Program(Rules::program),
    Compound(Rules::compound),
    StatementList(Rules::statementList),
    Statement(Rules::statement),
    Assigment(Rules::assignment),
    NoOperation(Rules::noOperation),
    Expression(Rules::expression),
    Term(Rules::term),
    Factor(Rules::factor),
    Variable(Rules::variable),
    Modifier(Rules::modifiers),
}