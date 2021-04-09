package com.kron.parse.structure.visitor

import com.kron.parse.structure.operands.Operation

/**
 * This allows for a very abstract way of calling methods. THe visitor exposes a way to visit a certain node and
 * return the given result
 */
@FunctionalInterface
fun interface IVisitor {
    /** This allows us to execute arbitrary code upon visiting **/
    fun visit(node: Operation): Any

    /**This is used to "inject" a visit method**/
    companion object
}