package com.wakwau.xplore.core.storage.repository

import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.storage.operation.FileOperationResult

interface StorageRepository {
    suspend fun getAvailableStorage(location: StorageLocation): FileOperationResult<Long>
    suspend fun getTotalStorage(location: StorageLocation): FileOperationResult<Long>
    suspend fun getStorageLocations(): FileOperationResult<List<StorageLocation>>
}
