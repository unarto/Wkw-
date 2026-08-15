package com.wakwau.xplore.filemanager.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakwau.xplore.filemanager.ui.action.CopyOperationHandler
import com.wakwau.xplore.filemanager.ui.action.CreateDirectoryOperationHandler
import com.wakwau.xplore.filemanager.ui.action.DeleteOperationHandler
import com.wakwau.xplore.filemanager.ui.action.MoveOperationHandler
import com.wakwau.xplore.filemanager.ui.action.PanelNavigationHandler
import com.wakwau.xplore.filemanager.ui.action.PanelRefreshHandler
import com.wakwau.xplore.filemanager.ui.action.RenameOperationHandler
import com.wakwau.xplore.filemanager.ui.event.DualPaneEvent
import com.wakwau.xplore.filemanager.ui.reducer.DualPaneReducer
import com.wakwau.xplore.filemanager.ui.state.DualPaneState
import com.wakwau.xplore.filemanager.ui.state.PanelId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DualPaneViewModel(
    private val reducer: DualPaneReducer,
    private val refreshHandler: PanelRefreshHandler,
    private val navigationHandler: PanelNavigationHandler,
    private val copyHandler: CopyOperationHandler,
    private val moveHandler: MoveOperationHandler,
    private val deleteHandler: DeleteOperationHandler,
    private val renameHandler: RenameOperationHandler,
    private val createDirectoryHandler: CreateDirectoryOperationHandler
) : ViewModel() {

    private val _state = MutableStateFlow(DualPaneState())
    val state: StateFlow<DualPaneState> = _state.asStateFlow()

    fun dispatch(event: DualPaneEvent) {
        val newState = reducer.reduce(_state.value, event)
        _state.value = newState
        
        handleSideEffects(event)
    }

    private fun handleSideEffects(event: DualPaneEvent) {
        val stateSnapshot = _state.value
        
        when (event) {
            is DualPaneEvent.OpenLocation -> {
                viewModelScope.launch {
                    refreshHandler.loadDirectory(event.panelId, event.location)
                }
            }
            is DualPaneEvent.Refresh -> {
                val panel = if (event.panelId == PanelId.LEFT) stateSnapshot.leftPanel else stateSnapshot.rightPanel
                panel.currentLocation?.let { location ->
                    viewModelScope.launch {
                        refreshHandler.loadDirectory(event.panelId, location)
                    }
                }
            }
            is DualPaneEvent.NavigateUp -> {
                navigationHandler.handleNavigateUp(stateSnapshot, event.panelId)
            }
            is DualPaneEvent.CopySelected -> {
                viewModelScope.launch {
                    copyHandler.execute(stateSnapshot)
                }
            }
            is DualPaneEvent.MoveSelected -> {
                viewModelScope.launch {
                    moveHandler.execute(stateSnapshot)
                }
            }
            is DualPaneEvent.DeleteSelected -> {
                viewModelScope.launch {
                    deleteHandler.execute(stateSnapshot)
                }
            }
            is DualPaneEvent.RenameItem -> {
                viewModelScope.launch {
                    renameHandler.execute(stateSnapshot, event.itemId, event.newName)
                }
            }
            is DualPaneEvent.CreateDirectory -> {
                viewModelScope.launch {
                    createDirectoryHandler.execute(stateSnapshot, event.name)
                }
            }
            // other events don't have side effects that require orchestration
            else -> {}
        }
    }
}
