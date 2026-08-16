package com.wakwau.xplore.di

import com.wakwau.xplore.core.storage.error.StorageErrorMapper
import com.wakwau.xplore.core.storage.filesystem.LocalFileSystemBackend
import com.wakwau.xplore.core.storage.mapper.FileItemMapper
import com.wakwau.xplore.core.storage.metadata.FileMetadataReader
import com.wakwau.xplore.core.storage.repository.DirectoryRepository
import com.wakwau.xplore.core.storage.repository.DirectoryRepositoryImpl
import com.wakwau.xplore.core.storage.repository.FileRepository
import com.wakwau.xplore.core.storage.repository.FileRepositoryImpl

class StorageModule {
    private val localFileSystemBackend = LocalFileSystemBackend()
    private val fileMetadataReader = FileMetadataReader()
    private val fileItemMapper = FileItemMapper()
    private val storageErrorMapper = StorageErrorMapper()

    val directoryRepository: DirectoryRepository by lazy {
        DirectoryRepositoryImpl(
            localFileSystemBackend = localFileSystemBackend,
            fileMetadataReader = fileMetadataReader,
            fileItemMapper = fileItemMapper,
            storageErrorMapper = storageErrorMapper
        )
    }

    val fileRepository: FileRepository by lazy {
        FileRepositoryImpl(
            localFileSystemBackend = localFileSystemBackend,
            fileMetadataReader = fileMetadataReader,
            fileItemMapper = fileItemMapper,
            storageErrorMapper = storageErrorMapper
        )
    }
}
