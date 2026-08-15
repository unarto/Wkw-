package com.wakwau.xplore.core.storage.error

import com.wakwau.xplore.core.storage.operation.FileOperationError
import java.io.FileNotFoundException
import java.io.IOException

class StorageErrorMapper {
    fun map(throwable: Throwable): FileOperationError {
        return when (throwable) {
            is FileNotFoundException -> FileOperationError.NOT_FOUND
            is SecurityException -> FileOperationError.ACCESS_DENIED
            is IllegalArgumentException -> FileOperationError.INVALID_LOCATION
            is IOException -> {
                val message = throwable.message ?: ""
                when {
                    message.contains("ENOSPC", ignoreCase = true) -> FileOperationError.IO_ERROR
                    message.contains("EACCES", ignoreCase = true) -> FileOperationError.ACCESS_DENIED
                    message.contains("EEXIST", ignoreCase = true) -> FileOperationError.ALREADY_EXISTS
                    else -> FileOperationError.IO_ERROR
                }
            }
            is UnsupportedOperationException -> FileOperationError.NOT_SUPPORTED
            else -> FileOperationError.UNKNOWN
        }
    }
}
