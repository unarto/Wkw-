package com.wakwau.xplore.filemanager.ui.action

import com.wakwau.xplore.core.storage.operation.FileOperationResult
import com.wakwau.xplore.filemanager.ui.event.DualPaneEvent
import com.wakwau.xplore.filemanager.ui.state.DualPaneState
import com.wakwau.xplore.filemanager.usecase.CreateDirectoryUseCase
import kotlinx.coroutines.CancellationException

class CreateDirectoryOperationHandler(
    private val createDirectoryUseCase: CreateDirectoryUseCase,
    private val dispatch: (DualPaneEvent) -> Unit
) {
    suspend fun execute(state: DualPaneState, name: String) {
        val activePanel = state.activePanel
        val currentLocation = activePanel.currentLocation ?: return
        
        dispatch(DualPaneEvent.OperationStarted("Create Directory"))
        try {
            val result = createDirectoryUseCase(currentLocation, name)
            when (result) {
                is FileOperationResult.Success -> {
                    dispatch(DualPaneEvent.OperationSuccess("Directory created"))
                    dispatch(DualPaneEvent.Refresh(activePanel.id))
                }
                is FileOperationResult.Failure -> {
                    dispatch(DualPaneEvent.OperationFailed(result.error.name))
                }
                is FileOperationResult.Cancelled -> {
                    dispatch(DualPaneEvent.OperationCancelled)
                }
            }
        } catch (e: CancellationException) {
            throw e
        }
    }
}
