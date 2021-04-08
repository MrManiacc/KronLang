package com.kron.node

import com.kron.node.Node
import com.kron.node.nodes.Variable as Var
import com.kron.node.nodes.Function as Func
import com.kron.node.nodes.Class as Cls
import com.kron.node.nodes.Module as Mod
import com.kron.node.nodes.Compound as Comp
import kotlin.reflect.KClass

/**
 * Keeps track of all of the types of node, and all of it's meta data.
 */
enum class NodeType(val type: KClass<out Node>) {
    Variable(Var::class),
    Function(Func::class),
    Class(Cls::class),
    Module(Mod::class),
    Compound(Comp::class)
}