package com.kron.parse

import com.kron.parse.structure.operands.Operation
import com.kron.parse.structure.rule.*
import com.kron.parse.structure.rule.RuleType.*
import com.kron.token.Lexer
import com.kron.token.Token
import com.kron.token.TokenType
import java.lang.Exception
import java.util.*

/**
 * This will take the lexer and and parse the stack of [Token]
 */
class Parser(
    /**The lexer input*/
    private val lexer: Lexer,
    /**Specify the rules here**/
    rules: RuleSet.Builder = RuleSet.new()
            name "core ruleset"
            with Program
            with Compound
            with Statement
            with StatementList
            with Assigment
            with Expression
            with Term
            with Variable
            with NoOperation
            with Modifier
) {
    /**We reverse the stack of tokens**/
    private var tokens: Stack<Token> = Stack<Token>().apply { addAll(lexer.tokens.reversed()) }

    /**The ruleset can be customized**/
    private val ruleset: RuleSet = rules.build(this)

    /**
     * This will parse the lexer data
     */
    fun parse(type: RuleType): Operation {
        return ruleset.call(type)
    }

    /**
     * This will attempt to eat the current token if  it's the given type
     */
    fun eat(tokenType: TokenType? = null, vararg others: TokenType) {
        val token = tokens.peek()

        if (token?.type == tokenType) {
            tokens.pop()
            return
        } else {
            if (others.isNotEmpty()) {
                val last = others[others.lastIndex]
                val array = Array<TokenType>(others.size - 1) {
                    others[it]
                }
                eat(last, *array)
                return
            }
        }
        error("Current token ${token.type.name} invalid for type ${tokenType?.name}")
    }

    /*Simple mapping for getting the current token**/
    val current: Token?
        get() = try {
            tokens.peek()
        } catch (ex: Exception) {
                        println("Peeked invalid token ${ex.message}")
//            ex.printStackTrace()
            null
        }


}