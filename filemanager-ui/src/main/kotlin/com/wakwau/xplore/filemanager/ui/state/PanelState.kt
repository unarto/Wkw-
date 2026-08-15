package com.wakwau.xplore.filemanager.ui.state

import com.wakwau.xplore.core.storage.model.FileItem
import com.wakwau.xplore.core.storage.model.StorageLocation

enum class SortMode {
    NAME,
    SIZE,
    DATE,
    TYPE
}

enum class SortOrder {
    ASCENDING,
    DESCENDING
}

data class PanelState(
    val id: PanelId,
    val currentLocation: StorageLocation? = null,
    val items: List<FileItem> = emptyList(),
    val selectedItemIds: Set<String> = emptySet(),
    val sortMode: SortMode = SortMode.NAME,
    val sortOrder: SortOrder = SortOrder.ASCENDING,
    val isLoading: Boolean = false,
    val error: String? = null
)
