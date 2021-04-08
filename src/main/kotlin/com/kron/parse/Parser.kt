package com.kron.parse

import com.kron.node.Node
import com.kron.token.Lexer
import com.kron.token.Token
import com.kron.token.TokenType

/**
 * This will take the lexer and and parse the stack of [Token]
 */
class Parser(private val lexer: Lexer) {

    /**
     * This will parse the lexer data
     */
    fun parse() {
        val variable = parseVariable()

    }

    /**
     * This will parse the statements
     */
    private fun parseVariable(): Node {
        val keyToken = findToken(TokenType.TokenVal)
        val id = nextToken(keyToken)

    }

    private fun parseExpression(): Node {

    }

    /**
     * Gets the next token after the given index
     */
    private fun isNext(afterToken: Token, type: TokenType): Boolean {
        return nextToken(afterToken).type == type
    }

    /**
     * Gets the next token after the given index
     */
    private fun nextToken(afterToken: Token): Token {
        return lexer.tokens[afterToken.index + 1]
    }

    /**
     * This will pop the next token of the given type
     */
    private fun findToken(tokenType: TokenType, remove: Boolean = false, afterIndex: Int = 0): Token {
        if (afterIndex >= lexer.tokens.size - 1)
            return Token.EMPTY
        for (i in afterIndex until lexer.tokens.size) {
            val token = lexer.tokens[i]
            if (token.type == tokenType)
                return if (remove) lexer.tokens.removeAt(i)
                else token
        }
        return Token.EMPTY
    }

    /**
     * This pop the token from the lexer
     */
    private fun popToken(token: Token) {
        lexer.tokens.remove(token)
    }
}