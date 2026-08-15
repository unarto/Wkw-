package com.wakwau.xplore.filemanager.usecase

import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.storage.operation.FileOperationResult
import com.wakwau.xplore.core.storage.repository.FileRepository

class CopyFileUseCase(private val fileRepository: FileRepository) {
    suspend operator fun invoke(source: StorageLocation, destination: StorageLocation): FileOperationResult<Unit> {
        return fileRepository.copy(source, destination)
    }
}
