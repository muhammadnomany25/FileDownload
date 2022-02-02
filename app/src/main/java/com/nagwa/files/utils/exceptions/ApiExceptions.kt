package com.nagwa.files.utils.exceptions

/**
 * @Author: Muhammad Noamany
 * @Email: muhammadnoamany@gmail.com
 */

/**
 * Exception indicating that api failed to load
 */
class ApiException(private var exceptionMsg: String) : Exception() {
    override val message: String
        get() = exceptionMsg
}


/**
 * Not handled unexpected exception
 */
class UncaughtException(cause: Exception) : Exception(cause)