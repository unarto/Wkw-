package com.wakwau.xplore.filemanager.usecase

import com.wakwau.xplore.core.storage.model.FileItem
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.storage.operation.FileOperationResult
import com.wakwau.xplore.core.storage.repository.DirectoryRepository

class ListFilesUseCase(private val directoryRepository: DirectoryRepository) {
    suspend operator fun invoke(location: StorageLocation): FileOperationResult<List<FileItem>> {
        return directoryRepository.list(location)
    }
}
