package com.wakwau.xplore

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wakwau.xplore.filemanager.ui.presentation.DualPaneViewModel
import com.wakwau.xplore.filemanager.ui.screen.DualPaneFileManagerScreen

@Composable
fun XploreRoot() {
    val context = LocalContext.current
    val app = context.applicationContext as XploreApplication
    val viewModel: DualPaneViewModel = viewModel(
        factory = app.appCompositionRoot.dualPaneViewModelFactory
    )
    DualPaneFileManagerScreen(viewModel = viewModel)
}
