package com.kron.parse.structure.operands

import com.kron.modifiers.*
import com.kron.modifiers.Modifier.*
import com.kron.token.Token

/**
 * This will store a variable. It keeps track of the token type and token name.
 */
class VarOp(val identifier: Token) : Operation {
    /**Simply prints our variable op**/
    override fun toString(): String = "VariableOp(identifier='${identifier.value}')"
}