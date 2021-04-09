package com.kron.parse.structure.visitor

import com.kron.parse.structure.operands.*
import com.kron.parse.structure.operators.*
import kotlin.reflect.KClass

/**
 * This class allows for a concrete implementation/ fast store of the visitors. There are constant time look ups and its
 * decoupled. the only downside is having the create a new enum for each visitor, but the performance I think is going
 * to benefit form this
 */
enum class VisitorType(val forOp: KClass<out Operation>, val visitor: IVisitor) {
    BinaryOpVisitor(BinaryOp::class, Visitors::binary),
    NumberOpVisitor(NumberOp::class, Visitors::number),
    UnaryOpVisitor(UnaryOp::class, Visitors::unary),
    VariableVisitor(VariableOp::class, Visitors::unary),
}