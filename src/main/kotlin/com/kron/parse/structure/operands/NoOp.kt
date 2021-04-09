package com.kron.parse.structure.operands

/**
 * This is a no op. it does nothing.
 */
class NoOp(private val errorMessage: String? = null) : Operation {
    /**Allows us to add an error message**/
    override fun toString(): String = if (errorMessage == null) "NoOp()" else "NoOp($errorMessage)"

    companion object
}