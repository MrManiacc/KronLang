package com.kron.node.reference

import com.kron.memory.Heap
import com.kron.isNodePresent
import com.kron.node.Node
import com.kron.toSimpleName
import java.util.*
import kotlin.reflect.KClass

/**
 * This is like a pointer in c++. It will get node from value from the heap if possible.
 */
class NodeRef<T : Node>(
    /**The generic class type of the node**/
    internal val type: KClass<T>,
    /**The memory location of the node**/
    internal var address: Long,
    /**The heap of to access from**/
    heap: Heap<T>?,
    /**This is the unique identifier for this reference. It will generated upon creation**/
    internal val referenceId: UUID = UUID.randomUUID(),
    /**The supplied node in that will override the use of the heap and address**/
    private val nodeIn: T? = null
) {

    /**
     * This is used as a reference
     */
    var heap: Heap<T>? = heap
        internal set(value) {
            value?.let ret@{
                heap?.get(address)?.remove(referenceId)
                val node = it[address]
                if (node?.add(this) == true) {
                    println("set the heap to $it")
                    field = it
                }
            }
        }

    /** This value acts as a getter for the node. It will be non-null but it will throw an exception upon trying to
     * access a null node. **/
    val node: T?
        get() {
            if (nodeIn != null) {
                if (nodeIn.add(this, false))
                    address = nodeIn.address
                return nodeIn
            }
            heap?.get(address)
                .takeIf {
                    if (type.isInstance(it)) {
                        it?.add(this, false) == true || it?.has(referenceId) == true
                    } else false
                }.apply { return if (type.isInstance(this) && this != null) this as T else null } as T?
        }

    /** This will destroy the node at the given address. It will also delete the address. **/
    fun destroy(deleteNode: Boolean = false): Boolean =
        if (isNodePresent) {
            if (deleteNode)
                heap?.remove(address);
            else
                node?.remove(referenceId)
            true
        } else false

    /**
     * This is used as a static way of creating empty refs
     */
    companion object {
        /**Gets the empty module instance**/
        internal val EMPTY = of<Node>(-1, null)

        /**This will generate a new node ref without having to type out the [KClass] type**/
        inline fun <reified T : Node> of(address: Long, heap: Heap<T>?) = of(T::class, address, heap)

        /**This will generate a new node ref**/
        fun <T : Node> of(type: KClass<T>, address: Long, heap: Heap<T>?) = NodeRef(type, address, heap)

        fun of(node: Node) = NodeRef(node::class, node.address, null)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NodeRef<*>

        if (address != other.address) return false
        if (type != other.type) return false
        if (referenceId != other.referenceId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = address.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + referenceId.hashCode()
        return result
    }

    override fun toString(): String {
        return "NodeRef(type=${type.toSimpleName}, address=$address, referenceId=$referenceId)"
    }

}