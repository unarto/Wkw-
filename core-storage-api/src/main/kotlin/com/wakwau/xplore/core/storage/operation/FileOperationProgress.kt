package com.wakwau.xplore.core.storage.operation

data class FileOperationProgress(
    val totalBytes: Long,
    val processedBytes: Long,
    val totalItems: Int,
    val processedItems: Int
) {
    val percentage: Float
        get() = if (totalBytes > 0) processedBytes.toFloat() / totalBytes else 0f
}
