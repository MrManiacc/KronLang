package com.kron.token

import com.google.common.primitives.Chars
import com.kron.dsl.*
import com.kron.exception.KronException
import com.kron.source.ISourceProvider
import com.kron.source.SourceData
import java.util.*

/**
 * This will turn create a new lexer. The contents can only be by the companion object
 */
class Lexer private constructor() {
    /**This wraps our content with an execution catcher so we don't have to null check everywhere. contents
     * should be non null at all times**/
    lateinit var contents: CharArray
        private set

    /**This will be passed to the parser, it's created by checking the characters of the source**/
    val tokens: Stack<Token> = Stack()

    /**Keeps track of the last token type**/
    var token: Token = Token(TokenType.TokenNone, 0, 0)
        private set

    /**The current lexer index**/
    var index = 0
        private set

    /**The size of the lexer. will throw []**/
    val size: Int
        @Throws(KronException::class) get() = contents.size

    /**Gets the lexer as a string**/
    val source: String
        @Throws(KronException::class) get() = contents.contentToString()

    /**
     * This will actually create each of the tokens.
     */
    fun tokenize(removalPredicate: (Token) -> Boolean = { it.type == TokenType.TokenNone || it.type == TokenType.TokenWhitespace }) {
        while (index < size) {
            val lastToken = token
            this.token = nextToken() ?: Token(TokenType.TokenNone, index, 1);
            this.index += token.length
            if (this.token.type == TokenType.TokenWhitespace && lastToken.type == TokenType.TokenWhitespace) {
                lastToken.length += token.length
                continue
            }
            tokens.add(token)
        }
        secondPass(removalPredicate)
    }

    /**
     * This will populate the tokens by parsing their type.
     */
    private fun secondPass(removeIf: (Token) -> Boolean) {
        var lastValidToken: TokenType = TokenType.TokenNone
        this.tokens.forEachIndexed { i, token ->
            val type = token.type
            if (token.isValidIndex && !token.isNone) {
                val read = token.length
                val prev = if (i == 0) Token.EMPTY else tokens[i - 1]
                val next = if (i + 1 < tokens.size) tokens[i + 1] else Token.EMPTY.apply { startIndex = size - 1 }
                val resolved = type.resolver?.resolve(this, read, prev, next, token)
                if (resolved != null)
                    token.value = resolved

            }
        }
        tokens.removeIf(removeIf)
    }

    /**
     * This will get the link at the given index or deafult value
     */
    operator fun get(index: Int): Char {
        return this.contents[index]
    }

    /**
     * This will attempt to get the next token, and increment the index if we do get it.
     */
    private fun nextToken(): Token? {
        var type: TokenType = TokenType.TokenNone
        var lengthRead = 0
        TokenType.forEach ret@{
            val result = it.doMatch(this)
            if (result > 0) {
                type = it
                lengthRead = result
                return@ret
            }
        }
        if (type == TokenType.TokenNone) {
            return null
        }
        return Token(type, index, lengthRead)
    }

    /**
     * This will peek at the next char. This should be checked to make sure it's not the last index
     * [distance] the distance away
     */
    fun peekSafe(distance: Int = 0): Char? {
        return try {
            peek(distance)
        } catch (ex: Exception) {
            ex.printStackTrace()
//            warn("${ex.message}")
            null
        }
    }

    /**
     * This will attempt to find the char at the given [distance] away from the current index. This will not increment
     * the index as we're just peeking.
     */
    @Throws(KronException::class) fun peek(distance: Int = 0): Char {
        val i = (index + distance)
        return contents[i]
    }

    /**
     * This will check to see if the given string matches the lexer state
     */
    fun isMatch(string: String): Boolean {

        val chars = string.toCharArray()
        chars.forEachIndexed { i, char ->
            if (!isCharacter(char, i))
                return false
        }
        return true
    }

    /**
     * Checks if the char is present by checking the offset
     */
    fun hasCharAt(offset: Int): Boolean {
        return this.index + offset < size
    }

    /**
     * Gets the current char with the [offset]
     */
    fun charAt(offset: Int = 0): Char {
        return contents[this.index + offset]
    }

    /**Check current character**/
    fun isCharacter(char: Char, offset: Int = 0): Boolean {
        return contents[index + offset] == char
    }

    /**
     * This will eat the current char if it's the given type]
     */
    fun eat(type: Char) {

    }

    /**
     * This will search for the next of the given [match] from the [start]
     */
    fun nextIndexOf(match: String, start: Int = this.index): Int {
        val copied = CharArray(size - start) { i: Int ->
            contents[start + i]
        }
        return Chars.indexOf(copied, match.toCharArray()) + start
    }

    /**
     * This will read raw in the given int range
     */
    fun readRaw(range: IntRange): String {
        val buffer = StringBuffer()
        for (i in range)
            buffer.append(this[i])
        return buffer.toString()
    }

    /**
     * This properly reads a give string
     */
    fun read(range: IntRange): String {
        val buffer = StringBuilder()
        for (i in range)
            buffer.append(this[i])

        return buffer.toString()
    }

    override fun toString(): String {
        var indent = 0
        val buffer = StringBuilder()
        for (i in tokens.indices) {
            val token = tokens[i]!!
            if (token.type == TokenType.TokenOpenBrace)
                indent += 3
            if (token.type == TokenType.TokenOpenAngleBracket)
                indent += 2
            buffer.append(token.toString(indent)).append("\n")
            if (token.type == TokenType.TokenCloseBrace)
                indent -= 3
            if (token.type == TokenType.TokenCloseAngleBracket)
                indent -= 2
        }
        return buffer.toString()
    }

    companion object {
        /**
         * This wil create a new lexer and gie it
         */
        fun of(provider: ISourceProvider): Lexer {
            return of(provider.provide().source)
        }

        /**
         * This wil create a new lexer and gie it
         */
        fun of(source: SourceData): Lexer {
            return of(source.source)
        }

        /**
         * A wrapper to create a lexer from a string provider
         */
        fun of(provider: () -> String): Lexer = of(provider())

        /**
         * A wrapper to create lexer with a string
         */
        fun of(contents: String): Lexer = of(contents.toCharArray())

        /**
         * Creates a new lexer of the given CharArray
         */
        fun of(contents: CharArray): Lexer {
            val lexer = com.kron.token.Lexer()
            lexer.contents = contents
            return lexer
        }


    }
}