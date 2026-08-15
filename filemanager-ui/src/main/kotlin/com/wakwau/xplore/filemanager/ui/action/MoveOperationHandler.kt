package com.wakwau.xplore.filemanager.ui.action

import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.storage.operation.FileOperationResult
import com.wakwau.xplore.filemanager.ui.event.DualPaneEvent
import com.wakwau.xplore.filemanager.ui.state.DualPaneState
import com.wakwau.xplore.filemanager.usecase.MoveFileUseCase
import kotlinx.coroutines.CancellationException

class MoveOperationHandler(
    private val moveFileUseCase: MoveFileUseCase,
    private val dispatch: (DualPaneEvent) -> Unit
) {
    suspend fun execute(state: DualPaneState) {
        val sourcePanel = state.activePanel
        val destPanel = state.inactivePanel
        
        val destLocation = destPanel.currentLocation ?: return
        val sourceItems = sourcePanel.items.filter { it.id in sourcePanel.selectedItemIds }
        if (sourceItems.isEmpty()) return
        
        dispatch(DualPaneEvent.OperationStarted("Move"))
        try {
            var anyFailure = false
            for (item in sourceItems) {
                val destPath = if (destLocation.path == "/") "/${item.name}" else "${destLocation.path}/${item.name}"
                val destLoc = StorageLocation(destPath, destLocation.rootId)
                val result = moveFileUseCase(item.location, destLoc)
                
                if (result is FileOperationResult.Failure) {
                    anyFailure = true
                    dispatch(DualPaneEvent.OperationFailed(result.error.name))
                    break
                } else if (result is FileOperationResult.Cancelled) {
                    dispatch(DualPaneEvent.OperationCancelled)
                    return
                }
            }
            if (!anyFailure) {
                dispatch(DualPaneEvent.OperationSuccess("Move completed"))
                dispatch(DualPaneEvent.Refresh(sourcePanel.id))
                dispatch(DualPaneEvent.Refresh(destPanel.id))
                dispatch(DualPaneEvent.ClearSelection(sourcePanel.id))
            }
        } catch (e: CancellationException) {
            throw e
        }
    }
}
