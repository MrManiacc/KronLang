package com.kron.node.nodes

import com.kron.node.Node
import com.kron.node.NodeType

class Module : Node(NodeType.Module) {
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