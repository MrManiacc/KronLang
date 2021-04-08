package com.kron.node

import com.google.common.collect.MapMaker
import com.kron.exception.KronExceptionType.InvalidHeapQuery
import com.kron.info
import com.kron.isEmpty
import com.kron.memory.Heap
import com.kron.node.mods.Modifiers
import com.kron.node.mods.Modifier.GlobalScope
import com.kron.node.mods.Modifier.PublicAccess
import com.kron.node.reference.NodeRef
import com.kron.toSimpleName
import com.kron.warn
import java.util.*

/**
 * This is the base for all other nodes. It can store a reference to it's parent, and a collection of children.
 */
abstract class Node(
    /**This part is very important as it helps us in running/parsing the node**/
    val type: NodeType,
    /**If null, this is a root node**/
    val parent: NodeRef<out Node> = NodeRef.EMPTY,
    /**These are our default modifiers**/
    modifiers: Modifiers? = null,
    /**This is the initial address it's -1 by default so it's an invalid memory location**/
    address: Long = -1,
) {

    /**The type of modifiers **/
    var modifiers: Modifiers = modifiers ?: (PublicAccess with GlobalScope).build()
        internal set

    /**The real address, it's initially set the [address] value, and has a private setter**/
    var address: Long = address
        private set

    /**We're a root node if **/
    val hasParent: Boolean get() = !parent.isEmpty

    /**The number of accessors of this node**/
    val references
        get() = referenceMap.count()

    /**Stores the internal reference id's**/
    private val referenceMap: MutableMap<UUID, NodeRef<out Node>> =
        MapMaker().weakValues().concurrencyLevel(4).initialCapacity(1000).makeMap()

    /**@return true if [referenceId] is present**/
    fun has(referenceId: UUID): Boolean = referenceMap.containsKey(referenceId)

    /**
     * This will add a reference and return true if it doesn't already exist.
     */
    fun add(
        node: NodeRef<out Node>,
        override: Boolean = false
    ): Boolean = if (this.referenceMap.containsKey(node.referenceId) && !override) false
    else {
        this[node.referenceId] = node
        true
    }

    /**
     * This will set the reference in the [referenceMap]
     */
    operator fun set(uuid: UUID, node: NodeRef<out Node>) {
        referenceMap[uuid] = node
    }

    /**
     * @returns true if node was removed
     */
    fun remove(uuid: UUID): NodeRef<out Node> {
        return referenceMap.remove(uuid) ?: NodeRef.EMPTY
    }

    /**
     * call this to trigger the onDestroyMethod
     */
    fun destroy(removeAddress: Boolean = true) {
        onDestroy()
        warn("Destroyed node with memory address $address")
        if (removeAddress) this.address = -1
    }

    /**
     * This is called when a node has been created
     */
    protected abstract fun onCreate()

    /**
     * This is called when all the references to the given node are lost. It's used for garbage cllection
     */
    protected abstract fun onDestroy()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        if (type != other.type) return false
        if (address != other.address) return false
        if (parent != other.parent) return false
        if (referenceMap != other.referenceMap) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        result = 31 * result + referenceMap.hashCode()
        return result
    }

    init {
        this.onCreate()
    }

}