package com.wakwau.xplore.filemanager.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wakwau.xplore.core.storage.model.FileType
import com.wakwau.xplore.core.ui.theme.DarkBackground
import com.wakwau.xplore.core.ui.theme.DarkBorder
import com.wakwau.xplore.filemanager.ui.component.FileManagerBreadcrumb
import com.wakwau.xplore.filemanager.ui.component.FileManagerStorageHeader
import com.wakwau.xplore.filemanager.ui.state.DualPaneState

@Composable
fun FileManagerContent(
    state: DualPaneState,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Split into Left and Right Panel placeholders for Phase 3A
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Left Panel (Weight 1f)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            FileManagerStorageHeader(
                location = state.leftPanel.currentLocation,
                subFoldersCount = state.leftPanel.items.count { it.type == FileType.DIRECTORY },
                subFilesCount = state.leftPanel.items.count { it.type == FileType.FILE }
            )
            FileManagerBreadcrumb(
                location = state.leftPanel.currentLocation,
                onNavigate = onNavigate
            )
            // Placeholder for FileList
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Left Panel Content Placeholder", color = Color.Gray)
            }
        }

        // Divider
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(DarkBorder)
        )

        // Right Panel (Weight 1f)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            FileManagerStorageHeader(
                location = state.rightPanel.currentLocation,
                subFoldersCount = state.rightPanel.items.count { it.type == FileType.DIRECTORY },
                subFilesCount = state.rightPanel.items.count { it.type == FileType.FILE }
            )
            FileManagerBreadcrumb(
                location = state.rightPanel.currentLocation,
                onNavigate = onNavigate
            )
            // Placeholder for FileList
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Right Panel Content Placeholder", color = Color.Gray)
            }
        }
    }
}
