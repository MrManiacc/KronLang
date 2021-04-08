package com.kron.utils

import mu.KotlinLogging
import kotlin.reflect.KClass

private val logger = KotlinLogging.logger {}

/** This should log ussing the kotlin logger **/
fun Any.info(text: String) {
//    logger.info(text)
    println("[info] $text")
}

/** This should log ussing the kotlin logger **/
fun Any.warn(text: String) {
//    logger.info(text)
    println("[warn] $text")
}

/**This gets the simple class for any object type**/
val Any?.toSimpleName: String
    get() {
        if (this is Class<*>)
            return this.simpleName
        if (this is KClass<*>)
            return this.java.toSimpleName
        if (this != null)
            return this::class.java.simpleName
        return "null"
    }

/**=============== [Char] extensions ===============*/

/**checks to see if the given number is a char, which can either be a digit or decimal **/
val Char.isNumber: Boolean get() = this.isDigit() || this == '.'

/**Simple inverse of isNumber**/
val Char.isNotNumber: Boolean get() = !isDigit() && this != '.'
