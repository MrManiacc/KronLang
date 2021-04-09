package com.kron.parse.structure.operands

import com.kron.modifiers.*

/**Simple stores modifiers**/
class ModOp(val builder: ModifierBuilder) : Operation {

    /**Allows us to keep the modifiers update**/
    val modifiers: Modifiers get() = builder.build()

    /**The modifiers already has a nice to string**/
    override fun toString(): String = "ModOp${modifiers}"

}