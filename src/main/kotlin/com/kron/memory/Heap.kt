package com.kron.memory

import com.google.common.collect.MapMaker
import com.kron.node.Node
import com.kron.node.mods.Modifier
import com.kron.node.mods.Modifier.*
import com.kron.node.mods.Modifiers

/***
 * The base holder for a node type
 */
open class Heap<T : Node>(
    /**Stores our per heap modifiers**/
    val modifier: Modifiers = GlobalScope with ImmutableFlag build PublicAccess //This will create a global scope
) {
    /** This is the index to the current "memory" for the next [nodes] of variables**/
    private var pointer: Long = 0

    /**We want to have at most 1,000,000 different pointers for this heap before we start recycling**/
    private val maxPointer = 1_000_000

    /**The variables, the id and an object**/
    private val nodes: MutableMap<Long, T> =
        MapMaker().weakValues().concurrencyLevel(4).initialCapacity(1000).makeMap()

    /**
     * This will attempt to find the next available address.
     */
    private fun nextAddress(): Long {
        if (!nodes.containsKey(pointer)) return pointer
        pointer++
        if (pointer >= maxPointer)
            pop(pointer)
        return nextAddress()
    }

    /**
     * Warning: This will override whatever is at the given address and return it.
     * [address] is the memory address relative to the current pointer
     */
    operator fun set(address: Long, value: T): T? {
        val old = nodes[address]
        nodes[address] = value
        return old
    }

    /**
     * This will attempt to remove the node at the address.
     */
    fun remove(address: Long): T? {
        return nodes.remove(address)
    }

    /**
     * This attempts to get a value off the heap at the given address
     */
    operator fun get(address: Long): T? {
        return nodes[address]
    }

    /**
     * This will push a node onto the heap and return the address
     */
    fun push(node: T): Long {
        val address = nextAddress()
        nodes[address] = node
        return address
    }

    /**
     * This will attempt to pop the last pushed node and will also update the current pointer to that position
     * [startAddress] is the first address to check for a non null value inside the nodes for removal. It will return the
     * node but the node will be destroyed. It will ignore any node that has references to it
     */
    fun pop(
        startAddress: Long = this.pointer,
        checkReferences: Boolean = false,
        updatePointer: Boolean = false,
        destroyNode: Boolean = true,
        nodeRetainAddress: Boolean = true
    ): T? {
        for (addr in startAddress downTo 1) {
            val node = this[addr] ?: continue
            if (checkReferences && node.references > 0) continue; //We want to check if we are checking the references
            if (updatePointer) this.pointer = node.address
            if (destroyNode) node.destroy(nodeRetainAddress)
            return node
        }
        return null
    }

    /**
     * Checks if the address is present
     */
    fun has(address: Long): Boolean {
        if (address < 0) return false
        return nodes.containsKey(address)
    }

    /**
     * This will free and clear all fo the nodes
     */
    fun destroy() {
        nodes.forEach { it.value.destroy() }
        nodes.clear()
    }
}