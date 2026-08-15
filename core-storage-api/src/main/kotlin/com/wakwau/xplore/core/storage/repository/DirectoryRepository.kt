package com.wakwau.xplore.core.storage.repository

import com.wakwau.xplore.core.storage.model.FileItem
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.storage.operation.FileOperationResult

interface DirectoryRepository {
    suspend fun list(location: StorageLocation): FileOperationResult<List<FileItem>>
    suspend fun create(location: StorageLocation, name: String): FileOperationResult<FileItem>
    suspend fun exists(location: StorageLocation): FileOperationResult<Boolean>
}
