package com.kron.token

/**Allows us to store a token with a given type**/
class Token(val type: TokenType, var index: Int, var length: Int, var value: String? = null) {

    /**The token is dynamic if we have a value**/
    val dynamic: Boolean get() = value != null

    /**
     * Allows for adding show/hide func to the index
     */
    fun toString(showIndex: Boolean = false, indentAmount: Int = 0): String {
        var indent = ""
        for (i in 0..indentAmount) indent += "\t"
        val prefix = if (showIndex) "[${index}]${type.name}" else type.name
        val value = if (dynamic) "length(${length}), value(${value})" else "length(${length})"
        return "$indent$prefix: $value"
    }

    /**
     * Native too String
     */
    override fun toString(): String {
        return toString(true)
    }

    companion object {
        /**Creates a new immutable token**/
        val EMPTY: Token
            get() = Token(TokenType.TokenNone, 0, 0, null)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        if (type != other.type) return false
        if (index != other.index) return false
        if (length != other.length) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + index
        result = 31 * result + length
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }

}