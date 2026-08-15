package com.wakwau.xplore.filemanager.ui.state

data class DualPaneState(
    val leftPanel: PanelState = PanelState(id = PanelId.LEFT),
    val rightPanel: PanelState = PanelState(id = PanelId.RIGHT),
    val activePanelId: PanelId = PanelId.LEFT,
    val operationState: OperationUiState = OperationUiState.Idle
) {
    val activePanel: PanelState
        get() = if (activePanelId == PanelId.LEFT) leftPanel else rightPanel

    val inactivePanel: PanelState
        get() = if (activePanelId == PanelId.LEFT) rightPanel else leftPanel
}
