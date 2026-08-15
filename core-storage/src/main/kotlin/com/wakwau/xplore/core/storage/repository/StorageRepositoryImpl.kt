package com.wakwau.xplore.core.storage.repository

import android.os.Environment
import android.os.StatFs
import com.wakwau.xplore.core.storage.error.StorageErrorMapper
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.storage.operation.FileOperationResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StorageRepositoryImpl(
    private val storageErrorMapper: StorageErrorMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : StorageRepository {

    override suspend fun getAvailableStorage(location: StorageLocation): FileOperationResult<Long> = withContext(ioDispatcher) {
        try {
            val stat = StatFs(location.path)
            val availableBytes = stat.availableBlocksLong * stat.blockSizeLong
            FileOperationResult.Success(availableBytes)
        } catch (e: kotlinx.coroutines.CancellationException) { return@withContext FileOperationResult.Cancelled } catch (e: Exception) {
            FileOperationResult.Failure(storageErrorMapper.map(e))
        }
    }

    override suspend fun getTotalStorage(location: StorageLocation): FileOperationResult<Long> = withContext(ioDispatcher) {
        try {
            val stat = StatFs(location.path)
            val totalBytes = stat.blockCountLong * stat.blockSizeLong
            FileOperationResult.Success(totalBytes)
        } catch (e: kotlinx.coroutines.CancellationException) { return@withContext FileOperationResult.Cancelled } catch (e: Exception) {
            FileOperationResult.Failure(storageErrorMapper.map(e))
        }
    }

    override suspend fun getStorageLocations(): FileOperationResult<List<StorageLocation>> = withContext(ioDispatcher) {
        try {
            val locations = mutableListOf<StorageLocation>()
            // Internal storage
            val internalPath = Environment.getExternalStorageDirectory().absolutePath
            locations.add(StorageLocation(path = internalPath, rootId = "primary"))
            
            // Further discovery via Context.getExternalFilesDirs would go here, 
            // but we'll stick to primary external storage for now to keep it simple.
            
            FileOperationResult.Success(locations)
        } catch (e: kotlinx.coroutines.CancellationException) { return@withContext FileOperationResult.Cancelled } catch (e: Exception) {
            FileOperationResult.Failure(storageErrorMapper.map(e))
        }
    }
}
