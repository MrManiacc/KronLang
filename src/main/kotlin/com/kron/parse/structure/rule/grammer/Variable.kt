package com.kron.parse.structure.rule.grammer

import com.kron.parse.Parser
import com.kron.parse.structure.operands.NoOp
import com.kron.parse.structure.operands.NumberOp
import com.kron.parse.structure.operands.Operation
import com.kron.parse.structure.operators.BinaryOp
import com.kron.parse.structure.operators.UnaryOp
import com.kron.parse.structure.rule.RuleSet
import com.kron.parse.structure.rule.RuleType
import com.kron.token.TokenType

object Variable {


    /**
     * This will index all of the variable declarations
     */
    fun declarations(parser: Parser, ruleSet: RuleSet?): Operation {
        return NoOp("unimplemented")
    }

    /**
     * This will
     */
    fun variableDeclaration(parser: Parser, ruleSet: RuleSet?): Operation {
        return NoOp("unimplemented")
    }
}