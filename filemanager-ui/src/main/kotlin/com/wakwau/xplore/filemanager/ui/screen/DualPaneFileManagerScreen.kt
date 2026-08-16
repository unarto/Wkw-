package com.wakwau.xplore.filemanager.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wakwau.xplore.filemanager.ui.presentation.DualPaneViewModel

@Composable
fun DualPaneFileManagerScreen(
    viewModel: DualPaneViewModel,
    modifier: Modifier = Modifier
) {
    FileManagerScreen(
        viewModel = viewModel,
        modifier = modifier
    )
}
