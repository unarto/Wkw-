package com.wakwau.xplore.filemanager.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wakwau.xplore.core.storage.model.FileItem
import com.wakwau.xplore.core.storage.model.FileType
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.ui.theme.DarkBackground
import com.wakwau.xplore.filemanager.ui.component.ActivePanelContent
import com.wakwau.xplore.filemanager.ui.component.PanelSwitcher
import com.wakwau.xplore.filemanager.ui.component.SideActionBar
import com.wakwau.xplore.filemanager.ui.event.DualPaneEvent
import com.wakwau.xplore.filemanager.ui.navigation.PanelGestureNavigation
import com.wakwau.xplore.filemanager.ui.state.DualPaneState
import com.wakwau.xplore.filemanager.ui.state.PanelId

@Composable
fun FileManagerContent(
    state: DualPaneState,
    onEvent: (DualPaneEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val activePanel = if (state.activePanelId == PanelId.LEFT) state.leftPanel else state.rightPanel

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        PanelSwitcher(
            activePanelId = state.activePanelId,
            onSelectPanel = { onEvent(DualPaneEvent.SetActivePanel(it)) }
        )
        Row(modifier = Modifier.weight(1f)) {
            PanelGestureNavigation(
                activePanelId = state.activePanelId,
                onSwipePanel = { onEvent(DualPaneEvent.SetActivePanel(it)) },
                modifier = Modifier.weight(1f)
            ) {
                ActivePanelContent(
                    panel = activePanel,
                    onNavigate = { onEvent(DualPaneEvent.OpenLocation(activePanel.id, it)) },
                    onItemClick = { item ->
                        if (item.type == FileType.DIRECTORY) {
                            onEvent(DualPaneEvent.OpenLocation(activePanel.id, item.location))
                        }
                    },
                    onItemLongClick = { item ->
                        onEvent(DualPaneEvent.SelectItem(activePanel.id, item.id))
                    },
                    onItemCheckToggle = { item ->
                        onEvent(DualPaneEvent.ToggleSelection(activePanel.id, item.id))
                    },
                    onRetry = { onEvent(DualPaneEvent.Refresh(activePanel.id)) }
                )
            }
            SideActionBar(onActionClick = { /* Handled via SideActionUiModel in next phase */ })
        }
    }
}
