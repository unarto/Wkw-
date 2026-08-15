package com.wakwau.xplore.core.storage.repository

import com.wakwau.xplore.core.storage.model.FileItem
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.storage.operation.FileOperationResult

interface FileRepository {
    suspend fun getFile(location: StorageLocation): FileOperationResult<FileItem>
    suspend fun exists(location: StorageLocation): FileOperationResult<Boolean>
    suspend fun delete(location: StorageLocation): FileOperationResult<Unit>
    suspend fun rename(location: StorageLocation, newName: String): FileOperationResult<FileItem>
    suspend fun copy(source: StorageLocation, destination: StorageLocation): FileOperationResult<Unit>
    suspend fun move(source: StorageLocation, destination: StorageLocation): FileOperationResult<Unit>
}
