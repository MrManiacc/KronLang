package com.kron.parse.structure.operands

import com.kron.modifiers.*
import com.kron.modifiers.Modifier.*
import com.kron.token.Token

/**
 * This will store a variable. It keeps track of the token type and token name.
 */
class StatementListOp(val statements: Array<Operation>) : Operation {
    override fun toString(): String {
        return "StatementListOp(statements=${statements.contentToString()})"
    }
}