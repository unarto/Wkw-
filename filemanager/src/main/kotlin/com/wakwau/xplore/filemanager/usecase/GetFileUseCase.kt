package com.wakwau.xplore.filemanager.usecase

import com.wakwau.xplore.core.storage.model.FileItem
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.storage.operation.FileOperationResult
import com.wakwau.xplore.core.storage.repository.FileRepository

class GetFileUseCase(private val fileRepository: FileRepository) {
    suspend operator fun invoke(location: StorageLocation): FileOperationResult<FileItem> {
        return fileRepository.getFile(location)
    }
}
