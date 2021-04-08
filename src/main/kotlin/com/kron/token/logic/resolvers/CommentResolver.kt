package com.kron.token.logic.resolvers

import com.kron.token.Lexer
import com.kron.token.Token
import com.kron.token.logic.IMatchingResolver
import kotlin.math.max

/**
 * Resolvers stores all of different types of resolvers that are more tedious.
 */

object CommentResolver : IMatchingResolver {

    /**
     * This should check if the matcher is valid
     */
    override fun check(lexer: Lexer): Int {
        val multi = getMultiComment(lexer)
        if (multi <= 0)
            return getSingleComment(lexer)
        return multi
    }

    private fun getMultiComment(lexer: Lexer): Int {
        if (lexer.checkMatch("/*")) {
            val stop = lexer.nextIndexOf("*/", lexer.index + 2) + 2
            return max(stop - lexer.index, 0)
        }
        return 0
    }

    private fun getSingleComment(lexer: Lexer): Int {
        if (lexer.checkMatch("//")) {
            val stop = lexer.nextIndexOf("\n", lexer.index + 2) + 1
            return max(stop - lexer.index, 0)
        }
        return 0
    }

    /**
     * This will get the value as a string or null. For a number this would be 12312.3232 etc.
     */
    override fun evaluate(lexer: Lexer, lengthRead: Int, previous: Token, next: Token): String {
        return lexer.readRaw(previous.index until next.index).trim().replace("\n", "")
    }

}
