package com.kron.parse.logic.operators

import com.kron.parse.logic.operands.NoOp
import com.kron.parse.logic.operands.Operation
import com.kron.utils.toSimpleName

/**
 * This is the base operator. All other operators will extend upon this.
 */
open class Operator(
    /**When trying to set the left **/
    var left: Operation = NoOp(),
    /**The right side of the operator**/
    var right: Operation = NoOp()
) : Operation
