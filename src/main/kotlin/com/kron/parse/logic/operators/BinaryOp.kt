package com.kron.parse.logic.operators

import com.kron.parse.logic.operands.Operation
import com.kron.token.TokenType
import com.kron.utils.toSimpleName

/**
 * This will take in a token type and then a left and right operand
 */
open class BinaryOp(left: Operation, right: Operation, val type: TokenType) : Operator(left, right) {
    override fun toString(): String {
        return "$toSimpleName(left=$left, right=$right, type=${type.name})"
    }
}

/**This class allows for multiplication between two [Operation]**/
class MulOp(left: Operation, right: Operation) : BinaryOp(left, right, TokenType.TokenMultiply)

/**This class allows for division between two [Operation]**/
class DivOp(left: Operation, right: Operation) : BinaryOp(left, right, TokenType.TokenDivide)

/**This class allows for addition between two [Operation]**/
class AddOp(left: Operation, right: Operation) : BinaryOp(left, right, TokenType.TokenPlus)

/**This class allows for subtraction between two [Operation]**/
class SubOp(left: Operation, right: Operation) : BinaryOp(left, right, TokenType.TokenMinus)