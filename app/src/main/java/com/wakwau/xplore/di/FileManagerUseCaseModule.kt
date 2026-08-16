package com.wakwau.xplore.di

import com.wakwau.xplore.core.storage.repository.DirectoryRepository
import com.wakwau.xplore.core.storage.repository.FileRepository
import com.wakwau.xplore.filemanager.usecase.CopyFileUseCase
import com.wakwau.xplore.filemanager.usecase.CreateDirectoryUseCase
import com.wakwau.xplore.filemanager.usecase.DeleteFileUseCase
import com.wakwau.xplore.filemanager.usecase.ListFilesUseCase
import com.wakwau.xplore.filemanager.usecase.MoveFileUseCase
import com.wakwau.xplore.filemanager.usecase.RenameFileUseCase

class FileManagerUseCaseModule(
    private val directoryRepository: DirectoryRepository,
    private val fileRepository: FileRepository
) {
    val listFilesUseCase: ListFilesUseCase by lazy { ListFilesUseCase(directoryRepository) }
    val copyFileUseCase: CopyFileUseCase by lazy { CopyFileUseCase(fileRepository) }
    val moveFileUseCase: MoveFileUseCase by lazy { MoveFileUseCase(fileRepository) }
    val deleteFileUseCase: DeleteFileUseCase by lazy { DeleteFileUseCase(fileRepository) }
    val renameFileUseCase: RenameFileUseCase by lazy { RenameFileUseCase(fileRepository) }
    val createDirectoryUseCase: CreateDirectoryUseCase by lazy { CreateDirectoryUseCase(directoryRepository) }
}
