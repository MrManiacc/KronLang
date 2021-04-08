package com.kron.exception

/***
 * The type of exception
 */
enum class KronExceptionType(internal val format: String) {
    NonDynamicToken(
        "attempted to retrieve value of type %0 for token of type %1"
    ),
    InvalidCast(
        "attempted to cast type `%0 `to type `%1` for %2"
    ),
    AttemptToIndexNullValue(
        "attempted to index null value named `%0` of type %1 that must be non null in class %2 "
    ),
    IndexOutOfBounds(
        "index `%0` out of bounds for array of type `%2 length `%1"
    ),
    NotFound(
        "failed to find %0 for token %1"
    ),
    InvalidNodeQuery(
        "attempted to access destroyed node reference of type `%0` from heap %1 at address `0x%2`"
    ),
    InvalidHeapQuery(
        "failed to find heap for node type `%0` at address `%1`"
    ),
    InvalidModifierUpdate(
        "tried to set modifier of type `%0` to value `%1`, for current value `%2`"
    ),
    General("%0")
    ;

    /**
     * This will replace the strings using the input format for the enum
     */
    private fun replace(strings: Array<out String>): String {
        var output: String = this.format
        strings.forEachIndexed { i, string ->
            output = output.replace("%$i", string)
        }
        return output
    }

    /**
     * This will attempt to throw the given exception
     */
    @Throws(KronException::class) fun throws(vararg format: String) {
        throw KronException(this, replace(format));
    }
}
