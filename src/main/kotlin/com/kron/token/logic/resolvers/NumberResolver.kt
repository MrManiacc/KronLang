package com.kron.token.logic.resolvers

import com.kron.dsl.isNotNumber
import com.kron.dsl.isNumber
import com.kron.token.Lexer
import com.kron.token.logic.IMatchingResolver

/**
 * Resolvers stores all of different types of resolvers that are more tedious.
 */

object NumberResolver : IMatchingResolver {
    /**
     * This should check if the matcher is valid
     */
    override fun check(lexer: Lexer): Int {
        var offset = 0
        if (!lexer.hasCharAt(offset)) return 0;
        var current = lexer.charAt(offset++)
        if (current.isNotNumber) return offset - 1
        while (lexer.hasCharAt(offset) && current.isNumber) {
            current = lexer.charAt(offset++)
            if (current.isNotNumber) return offset - 1
        }
        return offset
    }

}
