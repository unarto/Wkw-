package com.wakwau.xplore.filemanager.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.ui.components.StorageDiskBar

@Composable
fun FileManagerStorageHeader(
    location: StorageLocation?,
    subFoldersCount: Int = 0,
    subFilesCount: Int = 0,
    modifier: Modifier = Modifier
) {
    // In Phase 3A, we provide placeholder values for storage usage info 
    // since the real filesystem capacity query isn't fully wired to UI yet.
    StorageDiskBar(
        name = "Internal Storage",
        path = location?.path ?: "/",
        subFoldersCount = subFoldersCount,
        subFilesCount = subFilesCount,
        freeSpaceText = "12.5 GB", // Placeholder
        totalSpaceText = "64.0 GB", // Placeholder
        usedPercentage = 0.8f, // Placeholder
        isExternal = false,
        modifier = modifier
    )
}
