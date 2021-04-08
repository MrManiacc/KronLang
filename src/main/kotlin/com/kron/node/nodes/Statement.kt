package com.kron.node.nodes

import com.kron.node.Node
import com.kron.node.NodeType
import com.kron.node.reference.NodeRef

/**
 * This can store a value in memory at the given node location.
 */
class Statement(parent: Node) : Node(NodeType.Variable, NodeRef.EMPTY) {

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