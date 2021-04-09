package com.kron.parse.structure.rule.rules

import com.kron.parse.Parser
import com.kron.parse.structure.operands.NoOp
import com.kron.parse.structure.operands.NumberOp
import com.kron.parse.structure.operands.Operation
import com.kron.parse.structure.operators.BinaryOp
import com.kron.parse.structure.operators.UnaryOp
import com.kron.parse.structure.rule.IRule
import com.kron.parse.structure.rule.RuleSet
import com.kron.parse.structure.rule.RuleType.*
import com.kron.token.TokenType.*

/**
 * This object stores references to all of the different types of rules.
 */
object Rules {

    /**
     *  expr   : term ((PLUS | MINUS) term)
     *  term   : factor ((MUL | DIV) factor)
     *  factor : INTEGER | LPAREN expr RPAREN
     */
    fun expression(parser: Parser, ruleSet: RuleSet?): Operation {
        val rules = ruleSet ?: return NoOp()
        val node = rules.call(Term)
        val token = parser.current ?: return node
        return when (token.type) {
            TokenPlus -> {
                parser.eat(TokenPlus)
                BinaryOp(node, TokenPlus, rules.call(Term))
            }
            TokenMinus -> {
                parser.eat(TokenMinus)
                BinaryOp(node, TokenMinus, rules.call(Term))
            }
            TokenMultiply -> {
                parser.eat(TokenMultiply)
                BinaryOp(node, TokenMultiply, rules.call(Term))
            }
            TokenDivide -> {
                parser.eat(TokenDivide)
                BinaryOp(node, TokenDivide, rules.call(Term))
            }
            else -> node
        }
    }

    /**term : factor ((MUL | DIV) factor)**/

    fun term(parser: Parser, ruleSet: RuleSet?): Operation {
        val rules = ruleSet ?: return NoOp()
        var node = rules.call(Factor)
        val token = parser.current ?: return node
        if (token.type == TokenMultiply) {
            parser.eat(TokenMultiply)
            node = BinaryOp(node, TokenMultiply, rules.call(Factor))
            return node
        } else if (token.type == TokenDivide) {
            parser.eat(TokenDivide)
            node = BinaryOp(node, TokenDivide, rules.call(Factor))
            return node
        } else if (token.type == TokenPlus) {
            parser.eat(TokenPlus)
            node = BinaryOp(node, TokenPlus, rules.call(Factor))
            return node
        } else if (token.type == TokenMinus) {
            parser.eat(TokenMinus)
            node = BinaryOp(node, TokenMinus, rules.call(Factor))
            return node
        }
        return node
    }

    /**
     * This is the factor method.
     */
    fun factor(parser: Parser, ruleSet: RuleSet?): Operation {
        val rules = ruleSet ?: return NoOp()
        val token = parser.current ?: return NoOp()
        val node: Operation
        if (token.type == TokenPlus) {
            parser.eat(TokenPlus)
            node = UnaryOp(token, rules.call(Factor))
            return node
        } else if (token.type == TokenMinus) {
            parser.eat(TokenMinus)
            node = UnaryOp(token, rules.call(Factor))
            return node
        } else if (token.type == TokenNumber) {
            parser.eat(TokenNumber)
            node = NumberOp(token)
            return node
        } else if (token.type == TokenOpenParenthesis) {
            parser.eat(TokenOpenParenthesis)
            node = ruleSet.call(Expression)
            parser.eat(TokenCloseParenthesis)
            return node
        } else return NoOp()
    }


}