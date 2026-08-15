package com.wakwau.xplore.filemanager.ui.event

import com.wakwau.xplore.core.storage.model.FileItem
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.filemanager.ui.state.PanelId
import com.wakwau.xplore.filemanager.ui.state.SortMode
import com.wakwau.xplore.filemanager.ui.state.SortOrder

sealed class DualPaneEvent {
    // Navigation
    data class OpenLocation(val panelId: PanelId, val location: StorageLocation) : DualPaneEvent()
    data class NavigateUp(val panelId: PanelId) : DualPaneEvent()
    
    // Panel Focus
    data class SetActivePanel(val panelId: PanelId) : DualPaneEvent()
    
    // Selection
    data class SelectItem(val panelId: PanelId, val itemId: String) : DualPaneEvent()
    data class ToggleSelection(val panelId: PanelId, val itemId: String) : DualPaneEvent()
    data class ClearSelection(val panelId: PanelId) : DualPaneEvent()
    
    // Sorting
    data class SetSortMode(val panelId: PanelId, val sortMode: SortMode) : DualPaneEvent()
    data class SetSortOrder(val panelId: PanelId, val sortOrder: SortOrder) : DualPaneEvent()
    
    // Refresh
    data class Refresh(val panelId: PanelId) : DualPaneEvent()

    // Data Loading Result (From UseCase back to UI state)
    data class DirectoryLoaded(val panelId: PanelId, val location: StorageLocation, val items: List<FileItem>) : DualPaneEvent()
    data class DirectoryLoadFailed(val panelId: PanelId, val error: String) : DualPaneEvent()
    data class LoadingStarted(val panelId: PanelId) : DualPaneEvent()
    
    // Operation intents
    object CopySelected : DualPaneEvent()
    object MoveSelected : DualPaneEvent()
    object DeleteSelected : DualPaneEvent()
    data class RenameItem(val itemId: String, val newName: String) : DualPaneEvent()
    data class CreateDirectory(val name: String) : DualPaneEvent()
    
    // Operation Results
    data class OperationStarted(val operationName: String) : DualPaneEvent()
    data class OperationSuccess(val message: String) : DualPaneEvent()
    data class OperationFailed(val error: String) : DualPaneEvent()
    object OperationCancelled : DualPaneEvent()
    object ClearOperationState : DualPaneEvent()
}
