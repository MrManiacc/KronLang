package com.kron.parse.logic.operands

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

    /**Gets the number value as a double. It will cast the long to double if possible, or turn 0 if not**/
    val toDouble: Double
        get() {
            if (decimal != null)
                return decimal!!
            if (long != null)
                return long!!.toDouble()
            return 0.0
        }

    /**Gets the number value as a double. It will cast the long to double if possible, or turn 0 if not**/
    val toFloat: Float
        get() {
            if (decimal != null)
                return decimal!!.toFloat()
            if (long != null)
                return long?.toFloat() ?: 0.0f
            return 0.0f
        }

    /**Gets the number value as a double. It will cast the long to double if possible, or turn 0 if not**/
    val toLong: Long
        get() {
            if (long != null)
                return long!!
            if (decimal != null)
                return decimal!!.toLong()
            return 0L
        }

    /**Gets the number value as a double. It will cast the long to double if possible, or turn 0 if not**/
    val toInt: Int
        get() {
            if (long != null)
                return long!!.toInt()
            if (decimal != null)
                return decimal!!.toInt()
            return 0
        }

    /**
     * Outputs the decial and whole numbers
     */
    override fun toString(): String {
        return "NumberOp(decimal=$decimal, whole=$long)"
    }

}