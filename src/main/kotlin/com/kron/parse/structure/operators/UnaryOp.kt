package com.kron.parse.structure.operators

import com.kron.parse.structure.operands.Operation
import com.kron.token.Token
import com.kron.dsl.toName

/**
 * This will take in a token type and then a left and right operand
 */
open class UnaryOp(val token: Token, val expression: Operation) : Operation {
    override fun toString(): String {
        return "$toName(type=${token.type.name}, expression=$expression)"
    }
}
