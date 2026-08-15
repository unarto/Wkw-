package com.wakwau.xplore.filemanager.ui.state

import com.wakwau.xplore.core.storage.operation.FileOperationProgress

sealed class OperationUiState {
    object Idle : OperationUiState()
    data class Running(val operationName: String, val progress: FileOperationProgress? = null) : OperationUiState()
    data class Success(val message: String) : OperationUiState()
    data class Failure(val errorMessage: String) : OperationUiState()
    object Cancelled : OperationUiState()
}
