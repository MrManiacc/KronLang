package com.kron.parse.structure.operators

import com.kron.parse.structure.operands.Operation
import com.kron.dsl.toName

/**
 * This can stores an array of compounds. It will resize upon needing more by double the size.
 * It is mutable in the sense that once something is added it can't be removed.
 */
class CompoundOp(
    /**Adds our initial [statements] to the [children] if not null**/
    statements: Array<Operation>? = null,
    /**This is simply used as the initial value for the children**/
    private val children: MutableList<Operation>
) : Operator(), Iterable<Operation> {
    /** Exposes the current size of the children**/
    val size: Int get() = children.size

    /**
     * This will add a new operation
     */
    fun add(operation: Operation): Boolean = children.add(operation)

    /** Gets the child at the given index**/
    operator fun get(index: Int) = children[index]

    /** Returns an iterator over the elements of this object. **/
    override fun iterator(): Iterator<Operation> = children.iterator()

    /**Write out our current state**/
    override fun toString(): String = "$toName(size= $size, children=$children)"

    /**
     * TODO: start putting init on the bottom as it's the new kotlin standard. And I like the way it looks
     **/
    init {
        statements?.let { children.addAll(it) }
    }

}