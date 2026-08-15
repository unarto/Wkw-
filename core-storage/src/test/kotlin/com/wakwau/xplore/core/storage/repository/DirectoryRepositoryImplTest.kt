package com.wakwau.xplore.core.storage.repository

import com.wakwau.xplore.core.storage.error.StorageErrorMapper
import com.wakwau.xplore.core.storage.filesystem.LocalFileSystemBackend
import com.wakwau.xplore.core.storage.mapper.FileItemMapper
import com.wakwau.xplore.core.storage.metadata.FileMetadataReader
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.storage.operation.FileOperationError
import com.wakwau.xplore.core.storage.operation.FileOperationResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files

@OptIn(ExperimentalCoroutinesApi::class)
class DirectoryRepositoryImplTest {

    private lateinit var repository: DirectoryRepositoryImpl
    private lateinit var backend: LocalFileSystemBackend
    private lateinit var tempDir: File
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        backend = LocalFileSystemBackend()
        val metadataReader = FileMetadataReader()
        val itemMapper = FileItemMapper()
        val errorMapper = StorageErrorMapper()
        
        repository = DirectoryRepositoryImpl(
            localFileSystemBackend = backend,
            fileMetadataReader = metadataReader,
            fileItemMapper = itemMapper,
            storageErrorMapper = errorMapper,
            ioDispatcher = dispatcher
        )
        
        tempDir = Files.createTempDirectory("dir_repo_test").toFile()
    }

    @After
    fun teardown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun list_existingDirectory_returnsSuccess() = runTest {
        val file = File(tempDir, "file.txt")
        file.createNewFile()
        
        val result = repository.list(StorageLocation(tempDir.absolutePath))
        
        assertTrue(result is FileOperationResult.Success)
        val data = (result as FileOperationResult.Success).data
        assertEquals(1, data.size)
        assertEquals("file.txt", data[0].name)
    }

    @Test
    fun list_nonexistentDirectory_returnsFailure() = runTest {
        val result = repository.list(StorageLocation("${tempDir.absolutePath}/none"))
        
        assertTrue(result is FileOperationResult.Failure)
        assertEquals(FileOperationError.NOT_FOUND, (result as FileOperationResult.Failure).error)
    }

    @Test
    fun create_inExistingParent_returnsSuccess() = runTest {
        val result = repository.create(StorageLocation(tempDir.absolutePath), "newdir")
        
        assertTrue(result is FileOperationResult.Success)
        val dir = File(tempDir, "newdir")
        assertTrue(dir.exists())
        assertTrue(dir.isDirectory)
    }

    @Test
    fun create_duplicateDirectory_returnsFailure() = runTest {
        val dir = File(tempDir, "dupdir")
        dir.mkdir()
        
        val result = repository.create(StorageLocation(tempDir.absolutePath), "dupdir")
        
        assertTrue(result is FileOperationResult.Failure)
    }

    @Test
    fun exists_existingDirectory_returnsSuccessTrue() = runTest {
        val result = repository.exists(StorageLocation(tempDir.absolutePath))
        
        assertTrue(result is FileOperationResult.Success)
        assertEquals(true, (result as FileOperationResult.Success).data)
    }
}
