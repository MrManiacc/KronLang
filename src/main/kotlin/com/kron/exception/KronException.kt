package com.kron.exception

import java.lang.Exception

/**
 * a Throwable exception
 */
internal class KronException(type: KronExceptionType, message: String) :
    Exception("[Kron ${type.name} exception] $message") {
}