package com.kron.node.mods

import java.lang.StringBuilder

/***
 * This is the built type
 */
class Modifiers(private val mods: Int) {
    /**Gets the current values**/
    val values: List<Modifier> get() = enumValues<Modifier>().toList().filter { has(it) }

    /**True when the mods are 0 or less, meaning there's no [Modifier]**/
    val isEmpty: Boolean get() = mods <= 0

    /** This will return true if the given modifier is present* */
    infix fun has(modifier: Modifier): Boolean = (mods and modifier.modifier) == modifier.modifier

    /** Calls [block] block for valid modifiers**/
    fun forEach(block: (Modifier) -> Unit) = values.forEach(block)

    /** Calls [block] block for valid modifiers**/
    fun forEachIndexed(block: (Int, Modifier) -> Unit) = values.forEachIndexed(block)

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String {
        val current = this.values
        val builder = StringBuilder("Modifiers")
            .append("(")
            .append("Mods")
            .append("=")
            .append(mods)
            .append(", ")
        current.forEachIndexed { i, value ->
            builder.append(value.name)
                .append("=")
                .append(value.modifier)
            if (i != current.size - 1)
                builder.append(", ")

        }
        builder.append(")")
        return builder.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Modifiers

        if (mods != other.mods) return false

        return true
    }

    override fun hashCode(): Int {
        return mods
    }


}