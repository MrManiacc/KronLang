package com.kron.parse.structure.operators

import com.kron.parse.structure.operands.NoOp
import com.kron.parse.structure.operands.Operation
import com.kron.token.Token

/**
 * This is used to assign a value to a give variable
 */
class AssignOp(variableOp: Operation, val token: Token, expressionResult: Operation) :
    Operator(variableOp, expressionResult, "token=(${token.type.name})")