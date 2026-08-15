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

class FileRepositoryImpl(
    private val localFileSystemBackend: LocalFileSystemBackend,
    private val fileMetadataReader: FileMetadataReader,
    private val fileItemMapper: FileItemMapper,
    private val storageErrorMapper: StorageErrorMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FileRepository {

    override suspend fun getFile(location: StorageLocation): FileOperationResult<FileItem> = withContext(ioDispatcher) {
        try {
            val file = localFileSystemBackend.resolveFile(location)
            if (!file.exists()) {
                return@withContext FileOperationResult.Failure(
                    storageErrorMapper.map(java.io.FileNotFoundException("File not found: ${location.path}"))
                )
            }
            val metadata = fileMetadataReader.readMetadata(file)
            val type = if (file.isDirectory) FileType.DIRECTORY else FileType.FILE
            val fileItem = fileItemMapper.map(
                id = file.absolutePath,
                name = file.name,
                location = location,
                type = type,
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

    override suspend fun delete(location: StorageLocation): FileOperationResult<Unit> = withContext(ioDispatcher) {
        try {
            localFileSystemBackend.delete(location)
            FileOperationResult.Success(Unit)
        } catch (e: kotlinx.coroutines.CancellationException) { return@withContext FileOperationResult.Cancelled } catch (e: Exception) {
            FileOperationResult.Failure(storageErrorMapper.map(e))
        }
    }

    override suspend fun rename(location: StorageLocation, newName: String): FileOperationResult<FileItem> = withContext(ioDispatcher) {
        try {
            val targetFile = localFileSystemBackend.rename(location, newName)
            val metadata = fileMetadataReader.readMetadata(targetFile)
            val type = if (targetFile.isDirectory) FileType.DIRECTORY else FileType.FILE
            val newLocation = StorageLocation(path = targetFile.absolutePath, rootId = location.rootId)
            val fileItem = fileItemMapper.map(
                id = targetFile.absolutePath,
                name = targetFile.name,
                location = newLocation,
                type = type,
                metadata = metadata
            )
            FileOperationResult.Success(fileItem)
        } catch (e: kotlinx.coroutines.CancellationException) { return@withContext FileOperationResult.Cancelled } catch (e: Exception) {
            FileOperationResult.Failure(storageErrorMapper.map(e))
        }
    }

    override suspend fun copy(source: StorageLocation, destination: StorageLocation): FileOperationResult<Unit> = withContext(ioDispatcher) {
        try {
            localFileSystemBackend.copy(source, destination)
            FileOperationResult.Success(Unit)
        } catch (e: kotlinx.coroutines.CancellationException) { return@withContext FileOperationResult.Cancelled } catch (e: Exception) {
            FileOperationResult.Failure(storageErrorMapper.map(e))
        }
    }

    override suspend fun move(source: StorageLocation, destination: StorageLocation): FileOperationResult<Unit> = withContext(ioDispatcher) {
        try {
            localFileSystemBackend.move(source, destination)
            FileOperationResult.Success(Unit)
        } catch (e: kotlinx.coroutines.CancellationException) { return@withContext FileOperationResult.Cancelled } catch (e: Exception) {
            FileOperationResult.Failure(storageErrorMapper.map(e))
        }
    }
}
