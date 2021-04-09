package com.kron.parse.structure.operands

import com.kron.token.Token

/**
 * This stores a generic number with the token number type
 */
class NumberOp(
    token: Token
) : Operation {
    /**Stores the current long value of the parsed token**/
    private var decimal: Double? = null

    /**The integer**/
    private var long: Long? = null

    init {
        token.value?.let {
            this.decimal = it.toDoubleOrNull()
            this.long = it.toLongOrNull()
        }
    }

    /**Attempts to get the number**/
    val number: Number
        get() = when {
            this.decimal != null -> decimal!!
            this.long != null -> long!!
            else -> 0
        }

    /** Outputs the decimal and whole numbers **/
    override fun toString(): String = "NumberOp(decimal=$decimal, whole=$long)"


}