package com.kron.token

import com.kron.dsl.toString

/**Allows us to store a token with a given type**/
class Token(val type: TokenType, var startIndex: Int, var length: Int, var value: String? = null) {
    override fun toString(): String = toString(0)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        if (type != other.type) return false
        if (startIndex != other.startIndex) return false
        if (length != other.length) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + startIndex
        result = 31 * result + length
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }

    /**Gives The extensions**/
    companion object {}
}