package com.kron.parse.structure.rule

import com.kron.dsl.*
import com.kron.modifiers.*
import com.kron.modifiers.Modifier.*
import com.kron.parse.*
import com.kron.parse.structure.operands.*
import com.kron.parse.structure.operators.*
import com.kron.parse.structure.rule.RuleType.*
import com.kron.token.*
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
        var node = factor(parser, ruleSet)
        val token = parser.current ?: return node
        return when (token.type) {
            TokenMultiply -> {
                parser.eat(TokenMultiply)
                node = BinaryOp(node, TokenMultiply, rules.call(Factor))
                node
            }
            TokenDivide -> {
                parser.eat(TokenDivide)
                node = BinaryOp(node, TokenDivide, rules.call(Factor))
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
            TokenPlus -> {
                parser.eat(TokenPlus)
                node = UnaryOp(token, rules.call(Factor))
                return node
            }
            TokenMinus -> {
                parser.eat(TokenMinus)
                node = UnaryOp(token, rules.call(Factor))
                return node
            }
            TokenNumber -> {
                parser.eat(TokenNumber)
                node = NumberOp(token)
                return node
            }
            TokenOpenParenthesis -> {
                parser.eat(TokenOpenParenthesis)
                node = ruleSet.call(Expression)
                parser.eat(TokenCloseParenthesis)
                return node
            }
            TokenIdentifier -> {
                parser.eat(TokenIdentifier)
                return ruleSet.call(Variable)
            }
            else -> return NoOp()
        }
    }

    /**This is how a compound statement is processed**/
    fun compound(parser: Parser, ruleSet: RuleSet?): Operation {
        TODO("Parse teh compound")
    }

    /**This is how a statement list is processed**/
    fun statementList(parser: Parser, ruleSet: RuleSet?): Operation {
        val rules = ruleSet ?: return NoOp("Failed to find rules while in visitor statement list")
        val statement = rules.call(Statement)
        val results = ArrayList<Operation>().apply { add(statement) }
        while (parser.current?.isStatementSeparator == true) {
            parser.eat(TokenSemicolon, TokenNewLine) //So we eat either the semi or newline
            //            parser.eat(TokenNewLine) //So we eat either the semi or newline
            results.add(rules.call(Statement))
        }
        return StatementListOp(results.toTypedArray())
    }

    /**This is how a statement is processed**/
    fun statement(parser: Parser, ruleSet: RuleSet?): Operation {
        val rules = ruleSet ?: return NoOp("Failed to find rules while in visitor statement")
        return if (parser.current?.type == TokenOpenBrace) {
            parser.eat(TokenOpenBrace)
            rules.call(Compound)
        } else if (parser.current?.type == TokenVal || parser.current?.type == TokenIdentifier) {
            rules.call(Assigment)
        } else NoOp("Failed to find compound statement or assignment.")
    }

    /**This is how a assignment statement is processed**/
    fun assignment(parser: Parser, ruleSet: RuleSet?): Operation {
        val rules = ruleSet ?: return NoOp("Failed to find rules while in visitor assignment")
        val left = rules.call(Variable)
        val token = parser.current ?: return NoOp("Failed to find rules while in visitor assignment")
        parser.eat(TokenEquals)
        val right = rules.call(Expression)
        return AssignOp(left, token, right)
    }

    /**This is how a variable is processed**/
    fun variable(parser: Parser, ruleSet: RuleSet?): Operation {
        var nameId: Token? = null
        var typeId: Token? = null
        val modifiers = ModifierBuilder()
        if (parser.current?.type == TokenIdentifier) {
            nameId = parser.current!!
            parser.eat(TokenIdentifier)
        }
        if (parser.current?.type == TokenVal) {
            parser.eat(TokenVal)
            modifiers.with(ImmutableFlag)
        } else if (parser.current?.type == TokenVar) {
            parser.eat(TokenVar)
            modifiers.with(MutableFlag)
        }
        if (parser.current?.type == TokenIdentifier) {
            nameId = parser.current!!
            parser.eat(TokenIdentifier)
        }
        if (parser.current?.type == TokenColon) {
            parser.eat(TokenColon)
            val explicitType = parser.current ?: error("Expected identifier for val declaration!")
            if (explicitType.type != TokenIdentifier) error("Expected identifier for val declaration!")
            typeId = explicitType
            parser.eat(TokenIdentifier)
        }
        if (nameId == null) return NoOp("Failed to  find name identifier for variable declaration")
        return VariableOp(nameId, modifiers, typeId?.value)
    }

    /**This will create a a new modifiers builder and populate it if possible**/
    fun modifiers(parser: Parser, ruleSet: RuleSet?): Operation {
        var builder: ModifierBuilder? = null
        var current = parser.current ?: return NoOp("Failed to find when evaluating modifiers")
        while (current.isModifier) {
            val mod = current.modifier
            if (builder == null) builder = mod.builder
            parser.eat(null)
            builder = builder with mod
            current = parser.current ?: return ModOp(builder)
        }
        return builder?.let { ModOp(it) } ?: NoOp()
    }

    /**This is how a program is processed**/
    fun program(parser: Parser, ruleSet: RuleSet?): Operation {
        return NoOp("No program was parsed")
    }

    /**An empty type**/
    fun noOperation(parser: Parser, ruleSet: RuleSet?): Operation = NoOp("empty operation")
}
