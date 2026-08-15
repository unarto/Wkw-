package com.wakwau.xplore.filemanager.usecase

import com.wakwau.xplore.core.storage.model.FileMetadata
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.storage.operation.FileOperationResult
import com.wakwau.xplore.core.storage.repository.FileRepository

class GetFileMetadataUseCase(private val fileRepository: FileRepository) {
    suspend operator fun invoke(location: StorageLocation): FileOperationResult<FileMetadata> {
        return when (val result = fileRepository.getFile(location)) {
            is FileOperationResult.Success -> FileOperationResult.Success(result.data.metadata)
            is FileOperationResult.Failure -> result
            is FileOperationResult.Cancelled -> result
        }
    }
}
