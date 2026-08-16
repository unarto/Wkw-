package com.wakwau.xplore.filemanager.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wakwau.xplore.core.storage.model.FileItem
import com.wakwau.xplore.core.storage.model.FileType
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.filemanager.ui.list.FileList
import com.wakwau.xplore.filemanager.ui.list.FileListEmpty
import com.wakwau.xplore.filemanager.ui.list.FileListError
import com.wakwau.xplore.filemanager.ui.list.FileListLoading
import com.wakwau.xplore.filemanager.ui.state.PanelState

@Composable
fun ActivePanelContent(
    panel: PanelState,
    onNavigate: (StorageLocation) -> Unit,
    onItemClick: (FileItem) -> Unit,
    onItemLongClick: (FileItem) -> Unit,
    onItemCheckToggle: (FileItem) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        FileManagerStorageHeader(
            location = panel.currentLocation,
            subFoldersCount = panel.items.count { it.type == FileType.DIRECTORY },
            subFilesCount = panel.items.count { it.type == FileType.FILE }
        )
        FileManagerBreadcrumb(
            location = panel.currentLocation,
            onNavigate = { onNavigate(StorageLocation(path = it, rootId = panel.currentLocation?.rootId ?: "local")) }
        )
        when {
            panel.isLoading -> {
                FileListLoading(modifier = Modifier.weight(1f))
            }
            panel.error != null -> {
                FileListError(error = panel.error, onRetry = onRetry, modifier = Modifier.weight(1f))
            }
            panel.items.isEmpty() -> {
                FileListEmpty(modifier = Modifier.weight(1f))
            }
            else -> {
                FileList(
                    items = panel.items,
                    selectedItemIds = panel.selectedItemIds,
                    onItemClick = onItemClick,
                    onItemLongClick = onItemLongClick,
                    onItemCheckToggle = onItemCheckToggle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
