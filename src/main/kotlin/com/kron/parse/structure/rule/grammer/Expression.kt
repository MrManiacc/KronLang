package com.kron.parse.structure.rule.grammer

import com.kron.parse.Parser
import com.kron.parse.structure.operands.NoOp
import com.kron.parse.structure.operands.NumberOp
import com.kron.parse.structure.operands.Operation
import com.kron.parse.structure.operators.BinaryOp
import com.kron.parse.structure.operators.UnaryOp
import com.kron.parse.structure.rule.RuleSet
import com.kron.parse.structure.rule.RuleType
import com.kron.token.TokenType

object Expression {

    /**
     *  expr   : term ((PLUS | MINUS) term)
     *  term   : factor ((MUL | DIV) factor)
     *  factor : INTEGER | LPAREN expr RPAREN
     */
    fun expression(parser: Parser, ruleSet: RuleSet?): Operation {
        val rules = ruleSet ?: return NoOp()
        val node = rules.call(RuleType.Term)
        val token = parser.current ?: return node
        return when (token.type) {
            TokenType.TokenPlus -> {
                parser.eat(TokenType.TokenPlus)
                BinaryOp(node, TokenType.TokenPlus, rules.call(RuleType.Term))
            }
            TokenType.TokenMinus -> {
                parser.eat(TokenType.TokenMinus)
                BinaryOp(node, TokenType.TokenMinus, rules.call(RuleType.Term))
            }
            TokenType.TokenMultiply -> {
                parser.eat(TokenType.TokenMultiply)
                BinaryOp(node, TokenType.TokenMultiply, rules.call(RuleType.Term))
            }
            TokenType.TokenDivide -> {
                parser.eat(TokenType.TokenDivide)
                BinaryOp(node, TokenType.TokenDivide, rules.call(RuleType.Term))
            }
            else -> node
        }
    }

    /**term : factor ((MUL | DIV) factor)**/

    fun term(parser: Parser, ruleSet: RuleSet?): Operation {
        val rules = ruleSet ?: return NoOp()
        var node = rules.call(RuleType.Factor)
        val token = parser.current ?: return node
        return when (token.type) {
            TokenType.TokenMultiply -> {
                parser.eat(TokenType.TokenMultiply)
                node = BinaryOp(node, TokenType.TokenMultiply, rules.call(RuleType.Factor))
                node
            }
            TokenType.TokenDivide -> {
                parser.eat(TokenType.TokenDivide)
                node = BinaryOp(node, TokenType.TokenDivide, rules.call(RuleType.Factor))
                node
            }
            else -> node
        }
    }

    /**
     * This will use the order of operations to factor the expression
     */
    fun factor(parser: Parser, ruleSet: RuleSet?): Operation {
        val rules = ruleSet ?: return NoOp()
        val token = parser.current ?: return NoOp()
        val node: Operation
        when (token.type) {
            TokenType.TokenPlus -> {
                parser.eat(TokenType.TokenPlus)
                node = UnaryOp(token, rules.call(RuleType.Factor))
                return node
            }
            TokenType.TokenMinus -> {
                parser.eat(TokenType.TokenMinus)
                node = UnaryOp(token, rules.call(RuleType.Factor))
                return node
            }
            TokenType.TokenNumber -> {
                parser.eat(TokenType.TokenNumber)
                node = NumberOp(token)
                return node
            }
            TokenType.TokenOpenParenthesis -> {
                parser.eat(TokenType.TokenOpenParenthesis)
                node = ruleSet.call(RuleType.Expression)
                parser.eat(TokenType.TokenCloseParenthesis)
                return node
            }
            TokenType.TokenIdentifier -> {
                parser.eat(TokenType.TokenIdentifier)
                return ruleSet.call(RuleType.Variable)
            }
            else -> return NoOp()
        }
    }

}