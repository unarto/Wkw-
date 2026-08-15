package com.wakwau.xplore.filemanager.ui.reducer

import com.wakwau.xplore.filemanager.ui.event.DualPaneEvent
import com.wakwau.xplore.filemanager.ui.state.DualPaneState
import com.wakwau.xplore.filemanager.ui.state.OperationUiState
import com.wakwau.xplore.filemanager.ui.state.PanelId
import com.wakwau.xplore.filemanager.ui.state.PanelState

class DualPaneReducer {

    fun reduce(state: DualPaneState, event: DualPaneEvent): DualPaneState {
        return when (event) {
            is DualPaneEvent.SetActivePanel -> {
                state.copy(activePanelId = event.panelId)
            }
            is DualPaneEvent.OpenLocation -> {
                updatePanel(state, event.panelId) {
                    it.copy(currentLocation = event.location, isLoading = true, error = null)
                }
            }
            is DualPaneEvent.LoadingStarted -> {
                updatePanel(state, event.panelId) {
                    it.copy(isLoading = true, error = null)
                }
            }
            is DualPaneEvent.DirectoryLoaded -> {
                updatePanel(state, event.panelId) {
                    it.copy(
                        currentLocation = event.location,
                        items = event.items,
                        isLoading = false,
                        error = null,
                        selectedItemIds = emptySet() // clear selection on load
                    )
                }
            }
            is DualPaneEvent.DirectoryLoadFailed -> {
                updatePanel(state, event.panelId) {
                    it.copy(isLoading = false, error = event.error)
                }
            }
            is DualPaneEvent.SelectItem -> {
                updatePanel(state, event.panelId) {
                    it.copy(selectedItemIds = setOf(event.itemId))
                }
            }
            is DualPaneEvent.ToggleSelection -> {
                updatePanel(state, event.panelId) {
                    val newSelection = if (it.selectedItemIds.contains(event.itemId)) {
                        it.selectedItemIds - event.itemId
                    } else {
                        it.selectedItemIds + event.itemId
                    }
                    it.copy(selectedItemIds = newSelection)
                }
            }
            is DualPaneEvent.ClearSelection -> {
                updatePanel(state, event.panelId) {
                    it.copy(selectedItemIds = emptySet())
                }
            }
            is DualPaneEvent.SetSortMode -> {
                updatePanel(state, event.panelId) {
                    it.copy(sortMode = event.sortMode)
                }
            }
            is DualPaneEvent.SetSortOrder -> {
                updatePanel(state, event.panelId) {
                    it.copy(sortOrder = event.sortOrder)
                }
            }
            // Operation Results
            is DualPaneEvent.OperationStarted -> {
                state.copy(operationState = OperationUiState.Running(event.operationName))
            }
            is DualPaneEvent.OperationSuccess -> {
                state.copy(operationState = OperationUiState.Success(event.message))
            }
            is DualPaneEvent.OperationFailed -> {
                state.copy(operationState = OperationUiState.Failure(event.error))
            }
            is DualPaneEvent.OperationCancelled -> {
                state.copy(operationState = OperationUiState.Cancelled)
            }
            is DualPaneEvent.ClearOperationState -> {
                state.copy(operationState = OperationUiState.Idle)
            }
            // Intents that don't directly modify state synchronously without external result
            is DualPaneEvent.NavigateUp,
            is DualPaneEvent.Refresh,
            is DualPaneEvent.CopySelected,
            is DualPaneEvent.MoveSelected,
            is DualPaneEvent.DeleteSelected,
            is DualPaneEvent.RenameItem,
            is DualPaneEvent.CreateDirectory -> {
                state // Reducer does not perform side effects. It returns current state.
            }
        }
    }

    private fun updatePanel(
        state: DualPaneState,
        panelId: PanelId,
        updater: (PanelState) -> PanelState
    ): DualPaneState {
        return if (panelId == PanelId.LEFT) {
            state.copy(leftPanel = updater(state.leftPanel))
        } else {
            state.copy(rightPanel = updater(state.rightPanel))
        }
    }
}
