package com.kron.token.logic.resolvers

import com.kron.isNumber
import com.kron.token.Lexer
import com.kron.token.Token
import com.kron.token.logic.IMatchingResolver

/**
 * Resolvers stores all of different types of resolvers that are more tedious.
 */

object IdentifierResolver : IMatchingResolver {
    /**
     * This should check if the matcher is valid
     */
    override fun check(lexer: Lexer): Int {
        var distance = 0
        var current = lexer.peekSafe(++distance) ?: return 0
        if (current.isNumber || !current.isLetter() || current == ' ' || current == '\n' || current == '\\' || current == '/') return 0
        while (current.isLetterOrDigit()) {
            current = lexer.peekSafe(++distance) ?: return distance - 1
            if (!current.isLetterOrDigit()) return distance - 1
        }
        return distance
    }

    /**
     * This will get the value as a string or null. For a number this would be 12312.3232 etc.
     */
    override fun evaluate(lexer: Lexer, lengthRead: Int, previous: Token, next: Token): String? {
        return lexer.readRaw(previous.index + 1 until next.index).trim()
    }
}
