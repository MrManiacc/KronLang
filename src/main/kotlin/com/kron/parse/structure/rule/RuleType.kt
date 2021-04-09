package com.kron.parse.structure.rule

import com.kron.parse.structure.operands.*
import com.kron.parse.structure.rule.rules.Rules

/**
 * Stores the different rule types. These can be executed upon and chained.
 */
enum class RuleType(val rule: IRule) {
    Program(IRule { parser, set -> NoOp() }),
    Compound(IRule { parser, set -> NoOp() }),
    StatementList(IRule { parser, set -> NoOp() }),
    Statement(IRule { parser, set -> NoOp() }),
    Assigment(IRule { parser, set -> NoOp() }),
    NoOperation(IRule { parser, set -> NoOp() }),
    Expression(Rules::expression),
    Term(Rules::term),
    Factor(Rules::factor),
    Variable(IRule { parser, set -> NoOp() }),
}