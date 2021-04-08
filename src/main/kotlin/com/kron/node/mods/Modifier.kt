package com.kron.node.mods

/**
 * This is used to keep track of the visibility of nodes
 */
enum class Modifier {
    /**======Scope======**/
    GlobalScope,
    FileScope, //Basically static
    ModuleScope, //Basically like a kotlin object
    ClassScope, //kotlin class equivalent
    FunctionScope, //A method

    /**======Access level======**/
    PrivateAccess, //Can only be accessed by current scope
    ProtectedAccess,
    PublicAccess,
    FileOnlyAccess,
    ModuleOnlyAccess,

    /**======Misc flags======**/
    ImmutableFlag,
    MutableFlag,
    NullableFlag,
    NotNullableFlag;

    /**The current modifier**/
    val modifier: Int get() = 1 shr ordinal

    /**
     * This allows us to easily start the creation
     */
    infix fun with(other: Modifier): ModifierBuilder = ModifierBuilder(this.modifier) with other

    /**This will build with the given modifier type**/
    infix fun build(other: Modifier): Modifiers = ModifierBuilder(this.modifier) build other
}
