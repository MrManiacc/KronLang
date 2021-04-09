package com.kron.token.logic

import com.kron.dsl.range
import com.kron.token.Lexer
import com.kron.token.Token

/**
 * Used to combine a resolver and matcher in one
 */
interface IMatchingResolver {
    /** No need to modify this, however you can if wish to specify your own matcher type**/
    val matcher: ITokenMatcher
        get() = ITokenMatcher(this::check)

    /** No need to modify this, however you can if wish to specify your own matcher type**/
    val resolver: ITokenResolver
        get() = ITokenResolver(this::evaluate)

    /**
     * This should check if the matcher is valid
     */
    fun check(lexer: Lexer): Int

    /**
     * This will get the value as a string or null. For a number this would be 12312.3232 etc.
     */
    fun evaluate(lexer: Lexer, lengthRead: Int, previous: Token, next: Token, current: Token): String? {
        return lexer.read(current.range)
    }
}