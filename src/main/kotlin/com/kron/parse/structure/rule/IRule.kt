package com.kron.parse.structure.rule

import com.kron.parse.Parser
import com.kron.parse.structure.operands.Operation

/**
 * This creates a very abstract way of parsing the token data. This should output a single [Operation]
 */
fun interface IRule {
    /**Parses the current statement**/
    fun parse(parser: Parser, ruleSet: RuleSet?): Operation
}