package com.wakwau.xplore.filemanager.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.ui.theme.DarkBackground
import com.wakwau.xplore.filemanager.ui.component.FileManagerTopBar
import com.wakwau.xplore.filemanager.ui.event.DualPaneEvent
import com.wakwau.xplore.filemanager.ui.presentation.DualPaneViewModel
import com.wakwau.xplore.filemanager.ui.state.PanelId

@Composable
fun FileManagerScreen(
    viewModel: DualPaneViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        if (state.leftPanel.currentLocation == null) {
            viewModel.dispatch(DualPaneEvent.OpenLocation(PanelId.LEFT, StorageLocation("/storage/emulated/0", "local")))
        }
        if (state.rightPanel.currentLocation == null) {
            viewModel.dispatch(DualPaneEvent.OpenLocation(PanelId.RIGHT, StorageLocation("/storage/emulated/0", "local")))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            FileManagerTopBar(
                title = "X-plore",
                onSearchClick = { /* No-op for Phase 3B */ },
                onOverflowClick = { /* No-op for Phase 3B */ }
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        FileManagerContent(
            state = state,
            onEvent = viewModel::dispatch,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}
