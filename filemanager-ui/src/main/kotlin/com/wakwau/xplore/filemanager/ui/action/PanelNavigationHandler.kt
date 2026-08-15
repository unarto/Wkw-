package com.wakwau.xplore.filemanager.ui.action

import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.filemanager.ui.event.DualPaneEvent
import com.wakwau.xplore.filemanager.ui.state.DualPaneState
import com.wakwau.xplore.filemanager.ui.state.PanelId

class PanelNavigationHandler(
    private val dispatch: (DualPaneEvent) -> Unit
) {
    fun handleNavigateUp(state: DualPaneState, panelId: PanelId) {
        val panel = if (panelId == PanelId.LEFT) state.leftPanel else state.rightPanel
        val currentLocation = panel.currentLocation
        
        if (currentLocation != null && currentLocation.path.isNotEmpty() && currentLocation.path != "/") {
            val parentPath = currentLocation.path.substringBeforeLast("/")
            val newPath = if (parentPath.isEmpty()) "/" else parentPath
            val newLocation = StorageLocation(newPath, currentLocation.rootId)
            dispatch(DualPaneEvent.OpenLocation(panelId, newLocation))
        }
    }
}
