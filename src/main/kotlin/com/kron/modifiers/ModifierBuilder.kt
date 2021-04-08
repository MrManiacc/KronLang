package com.kron.modifiers

/**Used for creating modifiers**/
class ModifierBuilder(private val initialValue: Int = 0) {
    /**This will be updated by each call to the [with] function**/
    internal var modifiers: Int = initialValue

    /***
     * This infix will add bit shift using or
     */
    infix fun with(other: Modifier): ModifierBuilder {
        this.modifiers = this.modifiers or other.modifier
        return this
    }

    /***
     * This infix will add bit shift using or
     */
    infix fun with(other: ModifierBuilder): ModifierBuilder {
        if (other != this)
            this.modifiers = this.modifiers or other.modifiers
        return this
    }

    /**Builds with the current modifiers**/
    fun build(): Modifiers = Modifiers(this.modifiers)

    /** This creates a new instance of modifiers class **/
    infix fun build(other: Modifier): Modifiers = Modifiers(with(other).modifiers)

    /** This creates a new instance of modifiers class **/
    infix fun build(other: ModifierBuilder): Modifiers = Modifiers(with(other).modifiers)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ModifierBuilder

        if (initialValue != other.initialValue) return false
        if (modifiers != other.modifiers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = initialValue
        result = 31 * result + modifiers
        return result
    }


}
