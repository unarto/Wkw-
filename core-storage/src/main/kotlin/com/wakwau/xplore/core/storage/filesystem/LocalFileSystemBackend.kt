package com.wakwau.xplore.core.storage.filesystem

import com.wakwau.xplore.core.storage.model.StorageLocation
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class LocalFileSystemBackend {

    fun resolveFile(location: StorageLocation): File {
        return File(location.path)
    }

    fun exists(location: StorageLocation): Boolean {
        return resolveFile(location).exists()
    }

    fun list(location: StorageLocation): List<File> {
        val file = resolveFile(location)
        if (!file.exists() || !file.isDirectory) {
            throw FileNotFoundException("Directory not found or is not a directory: ${location.path}")
        }
        return file.listFiles()?.toList() ?: emptyList()
    }

    fun createFile(location: StorageLocation, name: String): File {
        val parent = resolveFile(location)
        if (!parent.exists() || !parent.isDirectory) {
            throw FileNotFoundException("Parent directory not found: ${location.path}")
        }
        val file = File(parent, name)
        if (file.exists()) {
            throw IOException("EEXIST: File already exists: $name")
        }
        if (!file.createNewFile()) {
            throw IOException("Failed to create file: $name")
        }
        return file
    }

    fun createDirectory(location: StorageLocation, name: String): File {
        val parent = resolveFile(location)
        if (!parent.exists() || !parent.isDirectory) {
            throw FileNotFoundException("Parent directory not found: ${location.path}")
        }
        val dir = File(parent, name)
        if (dir.exists()) {
            throw IOException("EEXIST: Directory already exists: $name")
        }
        if (!dir.mkdirs()) {
            throw IOException("Failed to create directory: $name")
        }
        return dir
    }

    fun delete(location: StorageLocation) {
        val file = resolveFile(location)
        if (!file.exists()) {
            throw FileNotFoundException("File not found: ${location.path}")
        }
        val deleted = if (file.isDirectory) {
            file.deleteRecursively()
        } else {
            file.delete()
        }
        if (!deleted) {
            throw IOException("Failed to delete file: ${location.path}")
        }
    }

    fun rename(location: StorageLocation, newName: String): File {
        val file = resolveFile(location)
        if (!file.exists()) {
            throw FileNotFoundException("File not found: ${location.path}")
        }
        val target = File(file.parentFile, newName)
        if (target.exists()) {
            throw IOException("EEXIST: Target already exists: $newName")
        }
        if (!file.renameTo(target)) {
            throw IOException("Failed to rename file to: $newName")
        }
        return target
    }

    fun copy(source: StorageLocation, destination: StorageLocation) {
        val sourceFile = resolveFile(source)
        val destFile = resolveFile(destination)
        
        if (!sourceFile.exists()) {
            throw FileNotFoundException("Source not found: ${source.path}")
        }
        
        if (sourceFile.isDirectory) {
            sourceFile.copyRecursively(destFile, overwrite = true)
        } else {
            sourceFile.copyTo(destFile, overwrite = true)
        }
    }

    fun move(source: StorageLocation, destination: StorageLocation) {
        val sourceFile = resolveFile(source)
        val destFile = resolveFile(destination)
        
        if (!sourceFile.exists()) {
            throw FileNotFoundException("Source not found: ${source.path}")
        }
        
        val renamed = sourceFile.renameTo(destFile)
        if (!renamed) {
            // fallback to copy and delete
            copy(source, destination)
            delete(source)
        }
    }
}
