package com.kron.token

import com.kron.exception.KronExceptionType.*
import com.kron.exception.KronException
import com.kron.token.logic.ITokenMatcher
import com.kron.token.logic.ITokenResolver
import com.kron.token.logic.IMatchingResolver
import com.kron.token.logic.resolvers.CommentResolver
import com.kron.token.logic.resolvers.IdentifierResolver
import com.kron.token.logic.resolvers.NumberResolver
import com.kron.token.logic.resolvers.StringResolver
import java.util.*

/** This allows us to keep a store storage type of the tokens internally. **/
enum class TokenType(
    /**This can be null for tokens like Id, that have to be parsed out.**/
    private val matcherIn: ITokenMatcher? = null,
    /**When this is non null, it is used to resolve the value of the token. If non null it will be sent to the token its
     * self, when null however, it will throw [KronException]*/
    private val resolverIn: ITokenResolver? = null,
    /**This is used as the comparator value for the [tokens] value. If null we use the ordinal. Small = closer to top,
     * it also accepts negatives.*/
    private val priorityIn: Int? = null,
    /**Combines a matcher and resolver**/
    private val matchingResolver: IMatchingResolver? = null,
) {
    TokenNone(
        { 0 }
    ),
    TokenWhitespace(
        {
            it.countMatchesOf(' ')
        }
    ),
    TokenComment(
        matchingResolver = CommentResolver,
    ),
    TokenOpenParenthesis(
        {
            if (it.checkMatch("(")) 1 else 0
        }),
    TokenCloseParenthesis(
        {
            if (it.checkMatch(")")) 1 else 0
        }),
    TokenPower(
        {
            if (it.checkMatch("^")) 1 else 0
        }),
    TokenMultiply(
        {
            if (it.checkMatch("*")) 1 else 0
        }),
    TokenDivide(
        {
            if (it.checkMatch("/")) 1 else 0
        }),
    TokenPlus(
        {
            if (it.checkMatch("+")) 1 else 0
        }),
    TokenMinus(
        {
            if (it.checkMatch("-")) 1 else 0
        }),
    TokenEquals(
        {
            if (it.checkMatch("=")) 1 else 0
        }),
    TokenEquality(
        {
            if (it.checkMatch("==")) 2 else 0
        }),
    TokenNewLine(
        {
            if (it.checkMatch("\n")) 1 else 0
        }),
    TokenModule(
        {
            if (it.checkMatch("module")) "module".length else 0
        }),
    TokenClass(
        {
            if (it.checkMatch("class")) "class".length else 0
        }),
    TokenInterface(
        {
            if (it.checkMatch("inter")) "inter".length else 0
        }),
    TokenNative(
        {
            if (it.checkMatch("extern")) "extern".length else 0
        }),
    TokenAnnotation(
        {
            if (it.checkMatch("@")) 1 else 0
        }),
    TokenOperatorOverload(
        {
            if (it.checkMatch("operator")) "operator".length else 0
        }),
    TokenOverride(
        {
            if (it.checkMatch("override")) "override".length else 0
        }),
    TokenSuper(
        {
            if (it.checkMatch("~")) 1 else 0
        }),
    TokenVal(
        {
            if (it.checkMatch("val")) 3 else 0
        }),
    TokenVar(
        {
            if (it.checkMatch("var")) 3 else 0
        }),
    TokenOpenAngleBracket(
        {
            if (it.checkMatch("<")) 1 else 0
        }),
    TokenCloseAngleBracket(
        {
            if (it.checkMatch(">")) 1 else 0
        }),
    TokenOpenBrace(
        {
            if (it.checkMatch("{")) 1 else 0
        }),
    TokenCloseBrace(
        {
            if (it.checkMatch("}")) 1 else 0
        }),
    TokenFunction(
        {
            if (it.checkMatch("fn")) 2 else 0
        }),
    TokenColon(
        {
            if (it.checkMatch(":")) 1 else 0
        }),
    TokenSemicolon(
        {
            if (it.checkMatch(";")) 1 else 0
        }),
    TokenComma(
        {
            if (it.checkMatch(",")) 1 else 0
        }),
    TokenString(
        matchingResolver = StringResolver
    ),

    /** This will attempt to find all numbers including decimals, negatives and real/imaginary numbers**/
    TokenNumber(
        matchingResolver = NumberResolver
    ),

    /** This is a rather complicated one that will resolve all ids. There's a few tokens that can come before **/
    TokenIdentifier(
        matchingResolver = IdentifierResolver
    ),

    ;

    /**Adds bindings for direct null *safe* access to the matcher **/
    private val matcher: ITokenMatcher
        get() {
            if (this.matcherIn != null) return matcherIn
            if (this.matchingResolver != null) return matchingResolver.matcher
            AttemptToIndexNullValue.throws("matcher", "ITokenMatcher", "KronTokenType")
            return ITokenMatcher { 0 } //This code shouldn't be called because of the exception that will be thrown
        }

    /**This references our resolver**/
    val resolver: ITokenResolver?
        get() {
            if (this.resolverIn != null) return resolverIn
            if (this.matchingResolver != null) return matchingResolver.resolver
            return null
        }

    /**Gets the current priority for the given token type based upon the enum paramters**/
    val priority: Int
        get() = if (this.priorityIn == null) this.ordinal else priorityIn

    /**
     * Gets the size of the lexer
     * @returns the number of character's that have been matched.
     */
    fun doMatch(lexer: Lexer): Int {
        return matcher.match(lexer)
    }

    /**
     * Neatly prints the token for readability
     */
    override fun toString(): String {
        return "TokenType(name=${this.name}, priority=${this.priority})"
    }

    companion object {
        /**This allows us access to all of the tokens in a sorted manor**/
        val tokens: Array<TokenType> = values()

        /**This wil sort the tokens and give us access. Because this is static, it should be dont upon first reference**/
        val sortedTokens = sortTokens(tokens)

        /**
         * This will get the array of tokens sorted. This will return an immutable array of sorted tokens
         */
        private fun sortTokens(unsorted: Array<TokenType>): Array<TokenType> {
            val types = arrayListOf<TokenType>()
                .apply { addAll(unsorted) }//Copy the unsorted array initially
            Collections.sort(types, this::compareTokens)
            return Array(types.size) { types[it] }
        }

        /**
         * This is what actually is internally used to sort the tokens array
         */
        private fun compareTokens(token1: TokenType, token2: TokenType): Int {
            return token2.priority.compareTo(token1.priority)
        }

        /**
         * This will attempt to get value at the given index, if [sorted] the [sortedTokens] will be used,
         * meaning it will be [priority] based indexing instead of [ordinal] based
         */
        operator fun get(index: Int, sorted: Boolean = true): TokenType {
            val size = if (sorted) sortedTokens.size else tokens.size //Realistically these two should be the same
            if (index < 0 || index >= size) IndexOutOfBounds.throws(
                "$index",
                "$size",
                "Array<kronTokenType>"
            )
            return if (sorted) sortedTokens[index]
            else tokens[index]
        }

        /**
         * This will iterate over each of either [tokens] or [sortedTokens]
         */
        fun forEach(sorted: Boolean = true, each: (TokenType) -> Unit) {
            if (sorted) sortedTokens.forEach(each)
            else tokens.forEach(each)
        }


    }


}