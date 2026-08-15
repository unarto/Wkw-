package com.wakwau.xplore.core.storage.filesystem

import com.wakwau.xplore.core.storage.model.StorageLocation
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files

class LocalFileSystemBackendTest {

    private lateinit var backend: LocalFileSystemBackend
    private lateinit var tempDir: File

    @Before
    fun setup() {
        backend = LocalFileSystemBackend()
        tempDir = Files.createTempDirectory("backend_test").toFile()
    }

    @After
    fun teardown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun resolveFile_returnsCorrectFile() {
        val location = StorageLocation(path = "${tempDir.absolutePath}/test.txt", rootId = "test")
        val file = backend.resolveFile(location)
        assertEquals(File("${tempDir.absolutePath}/test.txt").absolutePath, file.absolutePath)
    }

    @Test
    fun exists_whenFileExists_returnsTrue() {
        val file = File(tempDir, "exists.txt")
        file.createNewFile()
        val location = StorageLocation(path = file.absolutePath)
        assertTrue(backend.exists(location))
    }

    @Test
    fun exists_whenFileDoesNotExist_returnsFalse() {
        val location = StorageLocation(path = "${tempDir.absolutePath}/notexists.txt")
        assertFalse(backend.exists(location))
    }

    @Test
    fun list_whenDirectoryExists_returnsContents() {
        val file1 = File(tempDir, "file1.txt")
        file1.createNewFile()
        val dir1 = File(tempDir, "dir1")
        dir1.mkdir()

        val location = StorageLocation(path = tempDir.absolutePath)
        val files = backend.list(location)
        assertEquals(2, files.size)
        assertTrue(files.any { it.name == "file1.txt" })
        assertTrue(files.any { it.name == "dir1" })
    }

    @Test(expected = FileNotFoundException::class)
    fun list_whenDirectoryDoesNotExist_throwsException() {
        val location = StorageLocation(path = "${tempDir.absolutePath}/notexists")
        backend.list(location)
    }

    @Test
    fun createFile_whenParentExists_createsFile() {
        val location = StorageLocation(path = tempDir.absolutePath)
        val file = backend.createFile(location, "newfile.txt")
        assertTrue(file.exists())
        assertEquals("newfile.txt", file.name)
    }

    @Test(expected = FileNotFoundException::class)
    fun createFile_whenParentDoesNotExist_throwsException() {
        val location = StorageLocation(path = "${tempDir.absolutePath}/notexists")
        backend.createFile(location, "newfile.txt")
    }

    @Test(expected = IOException::class)
    fun createFile_whenFileAlreadyExists_throwsException() {
        val location = StorageLocation(path = tempDir.absolutePath)
        backend.createFile(location, "newfile.txt")
        backend.createFile(location, "newfile.txt")
    }
    
    @Test
    fun createDirectory_whenParentExists_createsDir() {
        val location = StorageLocation(path = tempDir.absolutePath)
        val dir = backend.createDirectory(location, "newdir")
        assertTrue(dir.exists())
        assertTrue(dir.isDirectory)
        assertEquals("newdir", dir.name)
    }
    
    @Test
    fun delete_whenFileExists_deletesFile() {
        val file = File(tempDir, "todelete.txt")
        file.createNewFile()
        val location = StorageLocation(path = file.absolutePath)
        backend.delete(location)
        assertFalse(file.exists())
    }
    
    @Test(expected = FileNotFoundException::class)
    fun delete_whenFileDoesNotExist_throwsException() {
        val location = StorageLocation(path = "${tempDir.absolutePath}/notexists.txt")
        backend.delete(location)
    }
    
    @Test
    fun rename_whenFileExists_renamesFile() {
        val file = File(tempDir, "oldname.txt")
        file.createNewFile()
        val location = StorageLocation(path = file.absolutePath)
        val newFile = backend.rename(location, "newname.txt")
        assertFalse(file.exists())
        assertTrue(newFile.exists())
        assertEquals("newname.txt", newFile.name)
    }
    
    @Test
    fun copy_whenSourceExists_copiesFile() {
        val sourceFile = File(tempDir, "source.txt")
        sourceFile.writeText("hello")
        val destFile = File(tempDir, "dest.txt")
        
        val sourceLoc = StorageLocation(path = sourceFile.absolutePath)
        val destLoc = StorageLocation(path = destFile.absolutePath)
        
        backend.copy(sourceLoc, destLoc)
        
        assertTrue(destFile.exists())
        assertEquals("hello", destFile.readText())
    }
    
    @Test
    fun move_whenSourceExists_movesFile() {
        val sourceFile = File(tempDir, "source_move.txt")
        sourceFile.writeText("world")
        val destFile = File(tempDir, "dest_move.txt")
        
        val sourceLoc = StorageLocation(path = sourceFile.absolutePath)
        val destLoc = StorageLocation(path = destFile.absolutePath)
        
        backend.move(sourceLoc, destLoc)
        
        assertFalse(sourceFile.exists())
        assertTrue(destFile.exists())
        assertEquals("world", destFile.readText())
    }
}
