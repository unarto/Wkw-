package com.wakwau.xplore.filemanager.ui.action

import com.wakwau.xplore.core.storage.operation.FileOperationResult
import com.wakwau.xplore.filemanager.ui.event.DualPaneEvent
import com.wakwau.xplore.filemanager.ui.state.DualPaneState
import com.wakwau.xplore.filemanager.usecase.DeleteFileUseCase
import kotlinx.coroutines.CancellationException

class DeleteOperationHandler(
    private val deleteFileUseCase: DeleteFileUseCase,
    private val dispatch: (DualPaneEvent) -> Unit
) {
    suspend fun execute(state: DualPaneState) {
        val sourcePanel = state.activePanel
        val sourceItems = sourcePanel.items.filter { it.id in sourcePanel.selectedItemIds }
        if (sourceItems.isEmpty()) return
        
        dispatch(DualPaneEvent.OperationStarted("Delete"))
        try {
            var anyFailure = false
            for (item in sourceItems) {
                val result = deleteFileUseCase(item.location)
                
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
                dispatch(DualPaneEvent.OperationSuccess("Delete completed"))
                dispatch(DualPaneEvent.Refresh(sourcePanel.id))
                dispatch(DualPaneEvent.ClearSelection(sourcePanel.id))
            }
        } catch (e: CancellationException) {
            throw e
        }
    }
}
