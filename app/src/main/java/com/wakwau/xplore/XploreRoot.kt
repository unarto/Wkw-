package com.wakwau.xplore

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wakwau.xplore.filemanager.ui.presentation.DualPaneViewModel
import com.wakwau.xplore.filemanager.ui.screen.DualPaneFileManagerScreen

@Composable
fun XploreRoot() {
    val viewModel: DualPaneViewModel = viewModel()
    DualPaneFileManagerScreen(viewModel = viewModel)
}
