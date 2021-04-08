package com.kron.token.logic

import com.kron.token.Lexer

/**
 * This allows us to add snippets of code to check the current Lexer state against the current token
 */
fun interface ITokenMatcher {
    /***
     * Will use the current state of the [Lexer] to check to see if the [forType] is a match
     * [lexer] the lexer testing against this the type to check against.
     *
     * returns -1 if not match was fun
     */
    fun match(lexer: Lexer): Int

}