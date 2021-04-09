/**
 *  This file is used for adding generial
 */
@file:kotlin.jvm.JvmMultifileClass
@file:kotlin.jvm.JvmName("CompilerExtKt")

package com.kron.dsl

import com.google.common.collect.Maps
import com.kron.parse.structure.operands.NoOp
import com.kron.parse.structure.operands.Operation
import com.kron.parse.structure.visitor.IVisitor
import com.kron.token.Token
import com.kron.token.TokenType
import com.kron.token.TokenType.*
import com.kron.parse.structure.visitor.VisitorType
import com.kron.parse.structure.visitor.visitors.Visitors
import kotlin.reflect.KClass

/**====================================== [TokenType] enum helpers====================================== */

/**true if the given token type is binary**/
val TokenType.isBinary: Boolean
    get() = this == TokenMultiply ||
            this == TokenDivide ||
            this == TokenPlus ||
            this == TokenMinus

/**Inverse of [isBinary]**/
val TokenType.isNotBinary: Boolean get() = !isBinary

/**This will get the default token for the given token type. If it doesn't have (right now it only works for
 * binary tokens), then it will return [EMPTY]**/
val TokenType.defaultToken: Token get() = if (isBinary) Token.binTokenFor(this) else Token.EMPTY

/**====================================== [Token] and [Token.Companion] ====================================== */

/**The ending location**/
val Token.endIndex: Int get() = startIndex + length - 1

/**Valid if we're not negative 1**/
val Token.isValidIndex: Boolean
    get() = this.startIndex > -1

/**True if the current token type is [TokenType.TokenNone]**/
val Token.isNone: Boolean get() = this.type == TokenNone

/**This will essentially check to make sure **/
val Token.isEmpty: Boolean get() = this == Token.EMPTY || isNone || (!isValidIndex && !isValuePresent)

/**This will essentially check to make sure **/
val Token.isNotEmpty: Boolean get() = !isEmpty

/**The token is dynamic if we have a value**/
val Token.isValuePresent: Boolean get() = value != null

/**Simply checks the type to make sure it's one of the binary types**/
val Token.isBinary: Boolean get() = this.type.isBinary

/**Inverse of [isBinary]**/
val Token.isNotBinary: Boolean get() = !isBinary

/**Simply gets the default range using [getRange] with 0 for the offset**/
val Token.range: IntRange get() = getRange(0, 0)

/**
 * This will get a range that will not be checked.
 */
fun Token.getRange(startOffset: Int = 0, stopOffset: Int = 0): IntRange = IntRange(
    startIndex + startOffset,
    endIndex + stopOffset
)

/**
 * Allows for adding show/hide func to the index
 */
fun Token.toString(indents: Int): String {
    var indent = ""
    for (i in 0 until indents) indent += "\t"
    return ("$indent${type.name}: ${if (isValuePresent) "length(${length}), value('${value}')" else "length(${length}"})") + ", range=$($startIndex..$endIndex)"
}

/**Creates a new immutable token**/
val Token.Companion.EMPTY: Token
    get() = Token(TokenNone, -1, -1, null)

/**Creates a new immutable multiplication token. We set the start index to zero because we don't know it and it's okay
 * as long as it's not less than -1, it's considered valid**/
val Token.Companion.BIN_MUL: Token
    get() = Token(TokenMultiply, 0, 1, "*") //Value isn't needed it's here for consistency

/**Creates a new immutable division token. We set the start index to zero because we don't know it and it's okay
 * as long as it's not less than -1, it's considered valid**/
val Token.Companion.BIN_DIV: Token
    get() = Token(TokenDivide, 0, 1, "/") //Value isn't needed it's here for consistency

/**Creates a new immutable addition token. We set the start index to zero because we don't know it and it's okay
 * as long as it's not less than -1, it's considered valid**/
val Token.Companion.BIN_ADD: Token
    get() = Token(TokenPlus, 0, 1, "+") //Value isn't needed it's here for consistency

/**Creates a new immutable subtraction token. We set the start index to zero because we don't know it and it's okay
 * as long as it's not less than -1, it's considered valid**/
val Token.Companion.BIN_SUB: Token
    get() = Token(TokenMinus, 0, 1, "-") //Value isn't needed it's here for consistency

/**Gets the given token for the specified type. If the type isn't specified we will return [EMPTY]**/
fun Token.Companion.binTokenFor(type: TokenType): Token = if (type.isNotBinary) EMPTY else when (type) {
    TokenMultiply -> BIN_MUL
    TokenDivide -> BIN_DIV
    TokenPlus -> BIN_ADD
    TokenMinus -> BIN_SUB
    else -> EMPTY
}

/**====================================== [Visitor] helpers====================================== */

/**This wil call the visitor for the given type**/
fun VisitorType.visit(operation: Operation): Any {
    return this.visitor.visit(operation)
}

/**This will attempt to cast the given value, will throw exception if cast fails**/
inline fun <reified T : Any> VisitorType.visitUntil(operation: Operation): T? {
    val result = this.visit(operation)
    if (result is NoOp) return null
    if (result is T) return result
    error("Failed to cast visitor result of type ${result.toName} to expected type ${T::class.toName}")
}

/**This will cache the visitors**/
private val cachedVisitors: MutableMap<KClass<out Operation>, IVisitor> =
    Maps.newHashMapWithExpectedSize(enumValues<VisitorType>().size)

/**So we don't have to keep making new [NoOp] instances**/
private val cachedNoOp = NoOp()

/**This will kickstart the visitation process. It will iterator over **/
internal fun Visitors.visit(operation: Operation): Any {
    var result = cachedVisitors[operation::class]
    if (result == null) {
        enumValues<VisitorType>().forEach { //Check each visitor type. Hence the cache, this could get expensive.
            if (it.forOp == operation::class) {
                cachedVisitors[operation::class] = it.visitor //Cache the visitor
                result = it.visitor
                return@forEach
            }
        }
    }
    if (result != null) return result!!.visit(operation)
    return cachedNoOp
}

/**
 * This simply gets rid of the need for the class type
 */
internal inline fun <reified T : Any> Visitors.visitUntil(operation: Operation): T? = Visitors.visitUntil(T::class, operation)

/**This will use the visit method to cache the visitor type and do other things**/
internal fun <T : Any> Visitors.visitUntil(type: KClass<T>, operation: Operation): T? {
    val result = this.visit(operation)
    if (result is NoOp) return null // not found
    if (type.java.isAssignableFrom(result.javaClass)) return result as T //This is checked with the if statement
    return visitUntil(type, operation)
}
