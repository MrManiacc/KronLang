package com.kron.parse.structure.operators

import com.kron.dsl.toName
import com.kron.parse.structure.operands.NoOp
import com.kron.parse.structure.operands.Operation

/**
 * This is the base operator. All other operators will extend upon this.
 */
abstract class Operator(
    /**When trying to set the left **/
    val left: Operation = NoOp(),
    /**The right side of the operator**/
    val right: Operation = NoOp(),
    /**Used for adding text to the toStrign**/
    protected var extraToString: String? = null
) : Operation {

    /** Simply writes out our operator to string, checking the token first.**/
    override fun toString(): String =
        if (extraToString != null) "$toName(left=$left, right=$right, $extraToString)"
        else "$toName(left=$left, right=$right)"

}
