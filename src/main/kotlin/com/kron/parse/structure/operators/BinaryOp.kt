package com.kron.parse.structure.operators

import com.kron.parse.structure.operands.Operation
import com.kron.token.TokenType

/**
 * This will take in a token type and then a left and right operand
 */
class BinaryOp(left: Operation, val type: TokenType, right: Operation) : Operator(left, right, "type=(${type.name})")