package com.wakwau.xplore.filemanager.usecase

import com.wakwau.xplore.core.storage.model.FileItem
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.storage.operation.FileOperationError
import com.wakwau.xplore.core.storage.operation.FileOperationResult

// Placeholder for future implementation
class CreateFileUseCase {
    suspend operator fun invoke(location: StorageLocation, name: String): FileOperationResult<FileItem> {
        return FileOperationResult.Failure(FileOperationError.NOT_SUPPORTED)
    }
}
