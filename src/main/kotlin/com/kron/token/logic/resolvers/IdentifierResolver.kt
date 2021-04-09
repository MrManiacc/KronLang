package com.kron.token.logic.resolvers

import com.kron.token.Lexer
import com.kron.token.logic.IMatchingResolver
import com.kron.dsl.isIdentifier
import com.kron.dsl.isNotIdentifier

/**
 * Resolvers stores all of different types of resolvers that are more tedious.
 */

object IdentifierResolver : IMatchingResolver {
    /**
     * This should check if the matcher is valid
     */
    override fun check(lexer: Lexer): Int {
        var offset = 0
        if (!lexer.hasCharAt(offset)) return 0;
        var current = lexer.charAt(offset++)
        if (current.isNotIdentifier || current.isDigit()) return offset - 1
        while (lexer.hasCharAt(offset) && current.isIdentifier) {
            current = lexer.charAt(offset++)
            if (current.isNotIdentifier) return offset - 1
        }
        return offset
    }
}
