package com.kron.parse.structure.operands

import com.kron.modifiers.*
import com.kron.modifiers.Modifier.*
import com.kron.token.Token

/**
 * This will store a variable. It keeps track of the token type and token name.
 */
class VariableOp(val identifier: Token, val modifiers: ModifierBuilder, val explicitType: String? = null) : Operation {
    /**Simply prints our variable op**/
    override fun toString(): String =
        if (explicitType == null) "VariableOp(identifier='${identifier.value}', modifiers=$modifiers)"
        else "VariableOp(identifier='${identifier.value}', modifiers=$modifiers, explicitType=($explicitType))"
}