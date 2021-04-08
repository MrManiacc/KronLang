package com.kron.source

import java.io.File
import java.io.InputStream
import java.io.IOException

import com.google.common.io.ByteSource
import com.kron.token.Lexer
import java.io.FileInputStream

/**
 * This allows us to easily resolve source code
 */
class SourceData private constructor(private val stream: InputStream? = null, private val file: File? = null) {

    /** Gets the source code **/
    val source: String
        get() {
            if (stream == null && file == null) return "NO_SOURCE"
            if (stream != null) {
                val byteSource: ByteSource = object : ByteSource() {
                    @Throws(IOException::class) override fun openStream(): InputStream {
                        return stream
                    }
                }
                return byteSource.asCharSource(Charsets.UTF_8).read()
            } else {
                val byteSource: ByteSource = object : ByteSource() {
                    @Throws(IOException::class) override fun openStream(): InputStream {
                        return FileInputStream(file!!)
                    }
                }
                return byteSource.asCharSource(Charsets.UTF_8).read()
            }
        }

    companion object {
        /**
         * This will create a raw source instance from the resourceName.
         * if not is exact we will append the initial /
         */
        fun fromResource(resourceName: String, addPrefix: Boolean = true, addPostfix: Boolean = false): SourceData {
            val pre = if (addPrefix) "/" else ""
            val post = if (addPostfix) ".kron" else ""
            return SourceData(Lexer::class.java.getResourceAsStream("$pre$resourceName$post"));
        }

        /**
         * Simply resads from file
         */
        fun fromFile(file: File): SourceData {
            return SourceData(file = file)
        }
    }

}