package com.kron.parse

import com.kron.parse.structure.operands.NoOp
import com.kron.parse.structure.operands.NumberOp
import com.kron.parse.structure.operands.Operation
import com.kron.parse.structure.operators.BinaryOp
import com.kron.parse.structure.operators.UnaryOp
import com.kron.parse.structure.rule.RuleSet
import com.kron.parse.structure.rule.RuleType
import com.kron.token.Lexer
import com.kron.token.Token
import com.kron.token.TokenType
import com.kron.token.TokenType.*
import java.lang.Exception
import java.util.*
import kotlin.math.exp

/**
 * This will take the lexer and and parse the stack of [Token]
 */
class Parser(private val lexer: Lexer) {
    /**We reverse the stack of tokens**/
    private var tokens: Stack<Token> = Stack<Token>().apply { addAll(lexer.tokens.reversed()) }

    /**The rule set for expressions**/
    val expressions =
        RuleSet.new() name "expresion rules" parser this with RuleType.Expression with RuleType.Term build RuleType.Factor

    /**
     * This will parse the lexer data
     */
    fun parse(): Operation {
        //        tokens = Stack<Token>().apply { addAll(lexer.tokens.reversed()) }
        //        val expression2 = this.expression()
        //        println("$expression\n$expression2")
        return expressions.call(RuleType.Expression)
    }

    /**
     * This will attempt to eat the current token if  it's the given type
     */
    fun eat(tokenType: TokenType) {
        val token = tokens.peek()
        if (token?.type == tokenType)
            tokens.pop()
        else {
            error("Current token ${token.type.name} invalid for type ${tokenType.name}")
        }
    }

    /*Simple mapping for getting the current token**/
    val current: Token?
        get() = try {
            tokens.peek()
        } catch (ex: Exception) {
            println("Peeked invalid token ${ex.message}")
            null
        }


}