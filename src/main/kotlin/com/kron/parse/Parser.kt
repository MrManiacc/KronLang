package com.kron.parse

import com.kron.parse.logic.operands.NoOp
import com.kron.parse.logic.operands.NumberOp
import com.kron.parse.logic.operands.Operation
import com.kron.parse.logic.operators.BinaryOp
import com.kron.parse.logic.operators.UnaryOp
import com.kron.token.Lexer
import com.kron.token.Token
import com.kron.token.TokenType
import com.kron.token.TokenType.*
import java.lang.Exception

/**
 * This will take the lexer and and parse the stack of [Token]
 */
class Parser(private val lexer: Lexer) {

    /**
     * This will parse the lexer data
     */
    fun parse() {
        val expression = this.expression()
        println(expression)
    }

    /** factor : INTEGER | LPAREN expr RPAREN **/
    private fun factor(): Operation {
        val token = peekNextToken()
        println(token)
        when (token.type) {
            TokenPlus -> {
                eat(TokenPlus)
                return UnaryOp(token, this.factor())
            }
            TokenMinus -> {
                eat(TokenMinus)
                return UnaryOp(token, this.factor())
            }
            TokenNumber -> {
                eat(TokenNumber)
                return NumberOp(token)
            }
            TokenOpenParenthesis -> {
                eat(TokenOpenParenthesis)
                val node = this.expression()
                eat(TokenCloseParenthesis)
                return node
            }
            else -> return NoOp()
        }
    }

    /**term : factor ((MUL | DIV) factor)**/
    private fun term(): Operation {
        var node = factor()
        if (peekNextToken().type == TokenMultiply) {
            eat(TokenMultiply)
            node = BinaryOp(node, factor(), TokenMultiply)
        } else if (peekNextToken().type == TokenDivide) {
            eat(TokenDivide)
            node = BinaryOp(node, factor(), TokenDivide)
        }
        return node
    }

    /**
     *  expr   : term ((PLUS | MINUS) term)
     *  term   : factor ((MUL | DIV) factor)
     *  factor : INTEGER | LPAREN expr RPAREN
     */
    private fun expression(): Operation {
        var node = term()
        if (peekNextToken().type == TokenPlus) {
            eat(TokenPlus)
            node = BinaryOp(node, factor(), TokenPlus)
        } else if (peekNextToken().type == TokenMinus) {
            eat(TokenMinus)
            node = BinaryOp(node, factor(), TokenMinus)
        }
        return node
    }

    /**
     * This will attempt to eat the current token if  it's the given type
     */
    private fun eat(tokenType: TokenType) {
        if (peekNextToken().type == tokenType)
            lexer.tokens.pop()
        else
            error("Current token invalid for type ${tokenType.name}")
    }

    /**
     * This will peek the next tokens
     */
    private fun peekNextToken(): Token {
        return try {
            lexer.tokens.peek()
        } catch (ex: Exception) {
            Token.EMPTY
        }
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