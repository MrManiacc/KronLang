package com.kron.parse.logic.operators

import com.kron.parse.logic.operands.Operation
import com.kron.token.Token
import com.kron.token.TokenType
import com.kron.utils.toSimpleName

/**
 * This will take in a token type and then a left and right operand
 */
open class UnaryOp(val token: Token, val expression: Operation) : Operation {
    override fun toString(): String {
        return "$toSimpleName(type=${token.type.name}, expression=$expression)"
    }
}
