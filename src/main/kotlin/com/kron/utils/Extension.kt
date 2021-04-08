package com.kron

import com.kron.memory.Heap
import com.kron.node.Node
import com.kron.node.mods.Modifier
import com.kron.node.mods.Modifiers
import com.kron.node.reference.NodeRef
import mu.KotlinLogging
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger {}

/** This should log ussing the kotlin logger **/
fun Any.info(text: String) {
//    logger.info(text)
    println("[info] $text")
}

/** This should log ussing the kotlin logger **/
fun Any.warn(text: String) {
//    logger.info(text)
    println("[warn] $text")
}

/**This gets the simple class for any object type**/
val Any?.toSimpleName: String
    get() {
        if (this is Class<*>)
            return this.simpleName
        if (this is KClass<*>)
            return this.java.toSimpleName
        if (this != null)
            return this::class.java.simpleName
        return "null"
    }

/**=============== [Char] extensions ===============*/

/**checks to see if the given number is a char, which can either be a digit or decimal **/
val Char.isNumber: Boolean get() = this.isDigit() || this == '.'

/**Simple inverse of isNumber**/
val Char.isNotNumber: Boolean get() = !isDigit() && this != '.'

/**This is used to add one node modifier to another and update the value**/
infix fun Modifier.and(other: Modifier): Modifier {
    return this
}

/**=============== [NodeRef] extensions ===============*/
/** True if the address and heap are set. This means we can use the [isNodePresent] variable**/
val NodeRef<out Node>.heapMods: Modifiers get() = this.heap?.modifier ?: Modifiers(-1)

/** True if the address and heap are set. This means we can use the [isNodePresent] variable**/
val NodeRef<out Node>.isValidAddress: Boolean get() = (this.address != -1L)

/** True if the address and heap are set. This means we can use the [isNodePresent] variable**/
val NodeRef<out Node>.isHeapValid: Boolean get() = heap != null

/** True if the given node is present inside the store **/
val NodeRef<out Node>.isNodePresent: Boolean get() = if (isValidAddress && isHeapValid) heap?.has(address) == true else false

/** Checks the current node ref against the empty node ref**/
val NodeRef<out Node>.isEmpty: Boolean get() = this == NodeRef.EMPTY

/**This will call the [block] only if it's present. **/
inline fun <reified T : Node> NodeRef<T>.ifNodePresent(block: (T) -> Unit) = if (isNodePresent) block(node!!) else Unit

/**This will call the [block] only if it's present. **/
inline fun <reified T : Node> NodeRef<T>.ifHeapPresent(block: (heap: Heap<T>) -> Unit) =
    if (isHeapValid) block(heap!!) else Unit

/**This will call the block when both the node and heap are present.*/
inline fun <reified T : Node> NodeRef<T>.ifPresent(block: (heap: Heap<T>, node: T) -> Unit) = if (isNodePresent && isHeapValid) block(
    heap!!, node!!
) else Unit

