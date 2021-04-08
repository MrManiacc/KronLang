package com.kron.source

/**
 * This allows for someone to easily provide a raw source
 */
fun interface ISourceProvider {
    /**
     * The raw source value should be proivded here
     */
    fun provide(): SourceData
}