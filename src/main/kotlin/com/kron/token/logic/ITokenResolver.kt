package com.kron.token.logic

import com.kron.token.Lexer
import com.kron.token.Token

/**
 * For any token that has a resolvable value like an id,
 * or number, we can provide a token value resolver
 */
fun interface ITokenResolver {
    /**
     * This should resolve a value for a resolvable token like a string, or number or identifier
     */
    fun resolve(lexer: Lexer, lengthRead: Int, previous: Token, next: Token): String?
}