package com.wakwau.xplore.core.storage.repository

import com.wakwau.xplore.core.storage.error.StorageErrorMapper
import com.wakwau.xplore.core.storage.filesystem.LocalFileSystemBackend
import com.wakwau.xplore.core.storage.mapper.FileItemMapper
import com.wakwau.xplore.core.storage.metadata.FileMetadataReader
import com.wakwau.xplore.core.storage.model.FileItem
import com.wakwau.xplore.core.storage.model.FileType
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.storage.operation.FileOperationResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DirectoryRepositoryImpl(
    private val localFileSystemBackend: LocalFileSystemBackend,
    private val fileMetadataReader: FileMetadataReader,
    private val fileItemMapper: FileItemMapper,
    private val storageErrorMapper: StorageErrorMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DirectoryRepository {

    override suspend fun list(location: StorageLocation): FileOperationResult<List<FileItem>> = withContext(ioDispatcher) {
        try {
            val files = localFileSystemBackend.list(location)
            val fileItems = files.map { file ->
                val metadata = fileMetadataReader.readMetadata(file)
                val type = if (file.isDirectory) FileType.DIRECTORY else FileType.FILE
                val itemLocation = StorageLocation(path = file.absolutePath, rootId = location.rootId)
                fileItemMapper.map(
                    id = file.absolutePath,
                    name = file.name,
                    location = itemLocation,
                    type = type,
                    metadata = metadata
                )
            }
            FileOperationResult.Success(fileItems)
        } catch (e: kotlinx.coroutines.CancellationException) { return@withContext FileOperationResult.Cancelled } catch (e: Exception) {
            FileOperationResult.Failure(storageErrorMapper.map(e))
        }
    }

    override suspend fun create(location: StorageLocation, name: String): FileOperationResult<FileItem> = withContext(ioDispatcher) {
        try {
            val dir = localFileSystemBackend.createDirectory(location, name)
            val metadata = fileMetadataReader.readMetadata(dir)
            val newLocation = StorageLocation(path = dir.absolutePath, rootId = location.rootId)
            val fileItem = fileItemMapper.map(
                id = dir.absolutePath,
                name = dir.name,
                location = newLocation,
                type = FileType.DIRECTORY,
                metadata = metadata
            )
            FileOperationResult.Success(fileItem)
        } catch (e: kotlinx.coroutines.CancellationException) { return@withContext FileOperationResult.Cancelled } catch (e: Exception) {
            FileOperationResult.Failure(storageErrorMapper.map(e))
        }
    }

    override suspend fun exists(location: StorageLocation): FileOperationResult<Boolean> = withContext(ioDispatcher) {
        try {
            FileOperationResult.Success(localFileSystemBackend.exists(location))
        } catch (e: kotlinx.coroutines.CancellationException) { return@withContext FileOperationResult.Cancelled } catch (e: Exception) {
            FileOperationResult.Failure(storageErrorMapper.map(e))
        }
    }
}
