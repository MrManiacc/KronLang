package com.kron.node.nodes

import com.kron.memory.Heap
import com.kron.node.Node
import com.kron.node.NodeType
import com.kron.node.mods.Modifiers
import com.kron.node.reference.NodeRef

/***
 * A class can have no parent
 */
class Class(
    modifiers: Modifiers
) : Node(NodeType.Class, NodeRef.EMPTY, modifiers) {
    /**
     * This is called when a node has been created
     */
    override fun onCreate() {

    }

    /**
     * This is called when all the references to the given node are lost. It's used for garbage cllection
     */
    override fun onDestroy() {

    }


}