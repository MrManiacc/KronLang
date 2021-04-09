package com.kron.modifiers

/**
 * This is used to keep track of the visibility of nodes
 */
enum class Modifier {
    /**======Internal use======**/
    NoModifiers,

    /**======Scope======**/
    GlobalScope,
    FileScope, //Basically static
    ModuleScope, //Basically like a kotlin object
    ClassScope, //kotlin class equivalent
    FunctionScope, //A method

    /**======Access level======**/
    PrivateAccess, //Can only be accessed by current scope
    PublicAccess,
    FileOnlyAccess, //Can be access by all modules in file
    ModuleOnlyAccess, //Can be accessed by anything that has a reference to given module
    NativeAccess, //The function is written in kotlin, and be can be accessed natively

    /**======Misc flags======**/
    OperatorOverrideFlag,
    OverrideFlag,
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

    /**Creates a new builder**/
    fun builder(): ModifierBuilder = ModifierBuilder(this.modifier)

    /**Creates a new modifier builder**/
    val builder: ModifierBuilder get() = builder()

}
