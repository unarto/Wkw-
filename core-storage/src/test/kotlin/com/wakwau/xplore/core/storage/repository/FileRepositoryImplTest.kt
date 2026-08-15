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
class FileRepositoryImplTest {

    private lateinit var repository: FileRepositoryImpl
    private lateinit var backend: LocalFileSystemBackend
    private lateinit var tempDir: File
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        backend = LocalFileSystemBackend()
        val metadataReader = FileMetadataReader()
        val itemMapper = FileItemMapper()
        val errorMapper = StorageErrorMapper()
        
        repository = FileRepositoryImpl(
            localFileSystemBackend = backend,
            fileMetadataReader = metadataReader,
            fileItemMapper = itemMapper,
            storageErrorMapper = errorMapper,
            ioDispatcher = dispatcher
        )
        
        tempDir = Files.createTempDirectory("file_repo_test").toFile()
    }

    @After
    fun teardown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun getFile_existingFile_returnsSuccess() = runTest {
        val file = File(tempDir, "test.txt")
        file.writeText("hello")
        
        val result = repository.getFile(StorageLocation(file.absolutePath))
        
        assertTrue(result is FileOperationResult.Success)
        val data = (result as FileOperationResult.Success).data
        assertEquals("test.txt", data.name)
    }

    @Test
    fun getFile_nonexistentFile_returnsFailure() = runTest {
        val result = repository.getFile(StorageLocation("${tempDir.absolutePath}/none.txt"))
        
        assertTrue(result is FileOperationResult.Failure)
        assertEquals(FileOperationError.NOT_FOUND, (result as FileOperationResult.Failure).error)
    }

    @Test
    fun getFile_existingDirectory_returnsSuccess() = runTest {
        val dir = File(tempDir, "test_dir")
        dir.mkdir()
        
        val result = repository.getFile(StorageLocation(dir.absolutePath))
        
        assertTrue(result is FileOperationResult.Success)
    }

    @Test
    fun exists_existingFile_returnsSuccessTrue() = runTest {
        val file = File(tempDir, "exists.txt")
        file.createNewFile()
        
        val result = repository.exists(StorageLocation(file.absolutePath))
        
        assertTrue(result is FileOperationResult.Success)
        assertEquals(true, (result as FileOperationResult.Success).data)
    }

    @Test
    fun exists_nonexistentFile_returnsSuccessFalse() = runTest {
        val result = repository.exists(StorageLocation("${tempDir.absolutePath}/none.txt"))
        
        assertTrue(result is FileOperationResult.Success)
        assertEquals(false, (result as FileOperationResult.Success).data)
    }

    @Test
    fun delete_existingFile_returnsSuccess() = runTest {
        val file = File(tempDir, "delete.txt")
        file.createNewFile()
        
        val result = repository.delete(StorageLocation(file.absolutePath))
        
        assertTrue(result is FileOperationResult.Success)
        assertTrue(!file.exists())
    }

    @Test
    fun delete_nonexistentFile_returnsFailure() = runTest {
        val result = repository.delete(StorageLocation("${tempDir.absolutePath}/none.txt"))
        
        assertTrue(result is FileOperationResult.Failure)
    }

    @Test
    fun rename_existingFile_validName_returnsSuccess() = runTest {
        val file = File(tempDir, "rename.txt")
        file.createNewFile()
        
        val result = repository.rename(StorageLocation(file.absolutePath), "newname.txt")
        
        assertTrue(result is FileOperationResult.Success)
        val newFile = File(tempDir, "newname.txt")
        assertTrue(newFile.exists())
    }

    @Test
    fun rename_existingFile_collision_returnsFailure() = runTest {
        val file = File(tempDir, "rename_col.txt")
        file.createNewFile()
        val existingTarget = File(tempDir, "target.txt")
        existingTarget.createNewFile()
        
        val result = repository.rename(StorageLocation(file.absolutePath), "target.txt")
        
        assertTrue(result is FileOperationResult.Failure)
    }

    @Test
    fun copy_existingFile_validDestination_returnsSuccess() = runTest {
        val file = File(tempDir, "copy.txt")
        file.writeText("content")
        val destFile = File(tempDir, "dest.txt")
        
        val result = repository.copy(StorageLocation(file.absolutePath), StorageLocation(destFile.absolutePath))
        
        assertTrue(result is FileOperationResult.Success)
        assertTrue(destFile.exists())
    }

    @Test
    fun move_existingFile_validDestination_returnsSuccess() = runTest {
        val file = File(tempDir, "move.txt")
        file.writeText("content")
        val destFile = File(tempDir, "dest_move.txt")
        
        val result = repository.move(StorageLocation(file.absolutePath), StorageLocation(destFile.absolutePath))
        
        assertTrue(result is FileOperationResult.Success)
        assertTrue(destFile.exists())
        assertTrue(!file.exists())
    }
}
