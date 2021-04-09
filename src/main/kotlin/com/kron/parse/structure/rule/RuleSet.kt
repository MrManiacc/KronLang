package com.kron.parse.structure.rule

import com.kron.parse.Parser
import com.kron.parse.structure.operands.NoOp
import com.kron.parse.structure.operands.Operation
import kotlin.collections.ArrayList

/**
 * This is the construct the rule set
 */
class RuleSet private constructor(
    /**The immutable list of rules for this rule set**/
    private val rules: Map<RuleType, IRule>,
    /**An array containing the key set of the rules, ordered**/
    private val order: Array<RuleType>,
    /**The name of the rule set (for debugging)**/
    private val name: String,
    /**If passed by the builder we use this first, otherwise it must be passed via the [parse] method**/
    private var parserIn: Parser? = null
) {

    /**
     * This will rune the code
     */
    internal fun parse(parserIn: Parser? = null) {
        val parser = this.parserIn ?: parserIn ?: error("Failed to find parser for rule set $this")
        this.parserIn = parser //Update the parse so [call] can use it
        order.forEach { value ->
            value.rule.parse(parser, this)
        }
    }

    /**True if the rule is present**/
    infix fun has(rule: RuleType): Boolean = rules.containsKey(rule)

    /**Tried to get the given rule or throws an error**/
    operator fun get(rule: RuleType): IRule = rules[rule] ?: error("Tried to access invalid rule for ruleset ${toString()}")

    /** This will attempt to call the given rule, returns [NoOp] if invalid **/
    fun call(rule: RuleType): Operation {
        return get(rule).parse(parserIn ?: return NoOp(), this)
    }

    /**For debugging**/
    override fun toString(): String = "RuleSet(name='$name', rules=$rules)"

    /**
     * This is used to create a new rule set
     */
    companion object {
        /**Creates the new builder instance**/
        fun new(): Builder = Builder()
    }

    /**
     * Used to build a new rule set
     */
    class Builder internal constructor() {
        private val ruleList: MutableList<Pair<RuleType, Int>> = ArrayList()

        private var parser: Parser? = null
        private var name: String = "unnamed ruleset"

        /** Appends the name **/
        infix fun name(name: String): Builder {
            this.name = name
            return this
        }

        /** Appends the parser **/
        infix fun parser(parser: Parser): Builder {
            this.parser = parser
            return this
        }

        /** Appends the given rule **/
        fun with(rule: RuleType, indexIn: Int = -1): Builder {
            val index = if (indexIn < 0) rule.ordinal else indexIn
            ruleList.add(Pair(rule, index))
            return this
        }

        /** Appends the given rule **/
        infix fun with(rule: RuleType): Builder = with(rule, -1)

        /** This is used to construct the new ruleset from the given rules **/
        infix fun build(rule: RuleType): RuleSet {
            with(rule)
            return build()
        }

        /**
         * This will buidl the rule set
         */
        fun build(): RuleSet {
            val rules = this.ruleList.associate { Pair(it.first, it.first.rule) }
            val order = this.ruleList.sortedBy { it.second }.map { it.first }.toTypedArray()
            return RuleSet(rules, order, name, parser)
        }
    }


}