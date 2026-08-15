package com.wakwau.xplore.core.storage.model

data class FileMetadata(
    val size: Long,
    val modifiedTime: Long,
    val createdTime: Long?,
    val isReadable: Boolean,
    val isWritable: Boolean,
    val isExecutable: Boolean,
    val isHidden: Boolean
)
