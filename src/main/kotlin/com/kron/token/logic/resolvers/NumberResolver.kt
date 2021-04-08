package com.kron.token.logic.resolvers

import com.kron.utils.isNotNumber
import com.kron.utils.isNumber
import com.kron.token.Lexer
import com.kron.token.Token
import com.kron.token.logic.IMatchingResolver

/**
 * Resolvers stores all of different types of resolvers that are more tedious.
 */

object NumberResolver : IMatchingResolver {
    /**
     * This should check if the matcher is valid
     */
    override fun check(lexer: Lexer): Int {
        var distance = 0
        var current: Char = lexer.peekSafe(++distance) ?: return 0
        if (current.isNotNumber) return 0
        while (current.isNumber) {
            //If the current peek safe is null, we return the dist
            current = lexer.peekSafe(++distance) ?: return (distance - 1)
        }
        //We take -1 of the distance because it needs to be offset
        return distance - 1
    }

    /**
     * This will get the value as a string or null. For a number this would be 12312.3232 etc.
     */
    override fun evaluate(lexer: Lexer, lengthRead: Int, previous: Token, next: Token): String {
        val start =
            if (previous != Token.EMPTY)
                previous.index + 1
            else 0
        val stop =
            if (next != Token.EMPTY)
                next.index + 1
            else 0
        return lexer.readRaw(start until stop);
    }
}
