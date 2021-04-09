package com.kron.parse.structure.operands

import com.kron.token.Token

/**
 * This will store a variable. It keeps track of the token type and token name.
 */
class VariableOp(val name: String, val token: Token) : Operation {
    /**Simply prints our variable op**/
    override fun toString(): String = "VariableOp(name='$name', token=$token)"
}