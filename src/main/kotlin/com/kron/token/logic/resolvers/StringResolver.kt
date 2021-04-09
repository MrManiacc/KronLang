package com.kron.token.logic.resolvers

import com.kron.dsl.getRange
import com.kron.token.Lexer
import com.kron.token.Token
import com.kron.token.logic.IMatchingResolver
import kotlin.math.max

/**
 * Resolvers stores all of different types of resolvers that are more tedious.
 */

object StringResolver : IMatchingResolver {

    /**
     * This should check if the matcher is valid
     */
    override fun check(lexer: Lexer): Int {
        if (lexer.isMatch("\"")) {
            val stop = lexer.nextIndexOf("\"", lexer.index + 1) + 1
            return max(stop - lexer.index, 0)
        }
        return 0
    }

    /**
     * This will get the value as a string or null. For a number this would be 12312.3232 etc.
     */
    override fun evaluate(lexer: Lexer, lengthRead: Int, previous: Token, next: Token, current: Token): String {
        // We want to get rid of of the quotes, but keep the range info in the token
        return lexer.read(current.getRange(1, -1))
    }
}
