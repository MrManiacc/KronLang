/**
 *  This file is used for adding generial
 */
@file:kotlin.jvm.JvmMultifileClass
@file:kotlin.jvm.JvmName("KotlinExtKt")

package com.kron.dsl

import com.kron.token.Token
import com.kron.token.TokenType
import kotlin.reflect.KClass

/**=============== Miscellaneous extensions ===============*/

/**This gets the simple class for any object type**/
val Any?.toName: String
    get() {
        if (this is Enum<*>)
            return this.name
        if (this is KClass<*>)
            return this.java.simpleName
        if (this is Class<*>)
            return this.simpleName
        if (this != null)
            return this::class.java.simpleName
        return "null"
    }

/**=============== [Char] extensions ===============*/

/**checks to see if the given number is a char, which can either be a digit or decimal **/
val Char.isNumber: Boolean get() = (this.isDigit() || this == '.') && (this != '+' && this != '-' && this != '*' && this != '/' && this != '(' && this != ')' && this != ' ')

/**Simple inverse of isNumber**/
val Char.isNotNumber: Boolean get() = !isDigit() && this != '.'

/**This will return true if the given value is an identifier**/
val Char.isIdentifier: Boolean get() = isLetterOrDigit()

/**Simple inverse of isIdentifier**/
val Char.isNotIdentifier: Boolean get() = !isIdentifier

/**=============== [Number] extensions ===============*/

/**This will first check to see if the numbers are doubles, if not it will try to cast them in order**/
operator fun Number.plus(other: Number): Number {
    if (this is Double && other is Double) return this + other
    if (this is Long && other is Long) return this + other
    if (this is Double && other is Long) return this + other
    if (this is Long && other is Double) return this + other
    error("Failed to to add type ${other.toName} to ${this.toName}")
}

/**This will first check to see if the numbers are doubles, if not it will try to cast them in order**/
operator fun Number.minus(other: Number): Number {
    if (this is Double && other is Double) return this - other
    if (this is Long && other is Long) return this - other
    if (this is Double && other is Long) return this - other
    if (this is Long && other is Double) return this - other
    error("Failed to to add type ${other.toName} to ${this.toName}")
}

/**This will first check to see if the numbers are doubles, if not it will try to cast them in order**/
operator fun Number.times(other: Number): Number {
    if (this is Double && other is Double) return this * other
    if (this is Long && other is Long) return this * other
    if (this is Double && other is Long) return this * other
    if (this is Long && other is Double) return this * other
    error("Failed to to add type ${other.toName} to ${this.toName}")
}

/**This will first check to see if the numbers are doubles, if not it will try to cast them in order**/
operator fun Number.div(other: Number): Number {
    if (this is Double && other is Double) return this / other
    if (this is Long && other is Long) return this / other
    if (this is Double && other is Long) return this / other
    if (this is Long && other is Double) return this / other
    error("Failed to to add type ${other.toName} to ${this.toName}")
}

/**This will do a unary plus on this given number**/
operator fun Number.unaryPlus(): Number {
    if (this is Double) return +this
    if (this is Long) return +this
    if (this is Double) return +this
    if (this is Long) return +this
    error("Failed execute unary on unsupported number '${this.toName}'")
}

/**This will do a unary minus on this given number**/
operator fun Number.unaryMinus(): Number {
    if (this is Double) return -this
    if (this is Long) return -this
    if (this is Double) return -this
    if (this is Long) return -this
    error("Failed execute unary on unsupported number '${this.toName}'")
}
