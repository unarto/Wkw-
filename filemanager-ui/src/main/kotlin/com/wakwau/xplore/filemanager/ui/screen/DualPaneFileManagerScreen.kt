package com.wakwau.xplore.filemanager.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.wakwau.xplore.core.ui.theme.DarkBackground
import com.wakwau.xplore.filemanager.ui.component.FileManagerTopBar
import com.wakwau.xplore.filemanager.ui.presentation.DualPaneViewModel

@Composable
fun DualPaneFileManagerScreen(
    viewModel: DualPaneViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            FileManagerTopBar(
                title = "WKW Xplore",
                onSearchClick = { /* No-op for Phase 3A */ },
                onOverflowClick = { /* No-op for Phase 3A */ }
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FileManagerContent(
                state = state,
                onNavigate = { /* No-op for Phase 3A, waiting for event setup */ },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
