package com.kron.parse.structure.visitor.visitors

import com.kron.dsl.*
import com.kron.parse.structure.operands.*
import com.kron.parse.structure.operators.*
import com.kron.token.TokenType.*

/**
 * This object stores all of the difference visitor types.
 */
internal object Visitors {
    /**This is just simply so we don't have to keep creating new instacnes**/
    private val noOperation = NoOp()

    /**This code will visit a binary node**/
    internal fun binary(it: Operation): Any {
        if (it !is BinaryOp) return noOperation
        val left: Number = visitUntil(it.left) ?: return noOperation
        val right: Number = visitUntil(it.right) ?: return noOperation
        return when (it.type) {
            TokenPlus -> left + right
            TokenMinus -> left - right
            TokenMultiply -> left * right
            TokenDivide -> left / right
            else -> noOperation
        }
    }

    /**This will evaluate the unary operation**/
    internal fun unary(it: Operation): Any {
        if (it !is UnaryOp) return noOperation
        val number: Number = visitUntil(it.expression) ?: return noOperation
        return when (it.token.type) {
            TokenPlus -> +number
            TokenMinus -> -number
            else -> noOperation
        }
    }

    /**This code will visit a binary node**/
    internal fun number(it: Operation): Any =
        if (it !is NumberOp) noOperation
        else it.number


}

