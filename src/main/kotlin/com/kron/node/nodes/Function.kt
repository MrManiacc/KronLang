package com.kron.node.nodes

import com.kron.node.Node
import com.kron.node.NodeType

class Function : Node(NodeType.Function) {
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