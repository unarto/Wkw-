package com.wakwau.xplore.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wakwau.xplore.filemanager.ui.action.CopyOperationHandler
import com.wakwau.xplore.filemanager.ui.action.CreateDirectoryOperationHandler
import com.wakwau.xplore.filemanager.ui.action.DeleteOperationHandler
import com.wakwau.xplore.filemanager.ui.action.MoveOperationHandler
import com.wakwau.xplore.filemanager.ui.action.PanelNavigationHandler
import com.wakwau.xplore.filemanager.ui.action.PanelRefreshHandler
import com.wakwau.xplore.filemanager.ui.action.RenameOperationHandler
import com.wakwau.xplore.filemanager.ui.event.DualPaneEvent
import com.wakwau.xplore.filemanager.ui.presentation.DualPaneViewModel
import com.wakwau.xplore.filemanager.ui.presentation.DualPaneViewModelFactory
import com.wakwau.xplore.filemanager.ui.reducer.DualPaneReducer

class FileManagerPresentationModule(
    private val useCaseModule: FileManagerUseCaseModule
) {
    private var activeDispatcher: ((DualPaneEvent) -> Unit)? = null
    private val dispatchBridge: (DualPaneEvent) -> Unit = { event ->
        activeDispatcher?.invoke(event)
    }

    val dualPaneReducer: DualPaneReducer by lazy { DualPaneReducer() }

    val panelRefreshHandler: PanelRefreshHandler by lazy {
        PanelRefreshHandler(useCaseModule.listFilesUseCase, dispatchBridge)
    }

    val panelNavigationHandler: PanelNavigationHandler by lazy {
        PanelNavigationHandler(dispatchBridge)
    }

    val copyOperationHandler: CopyOperationHandler by lazy {
        CopyOperationHandler(useCaseModule.copyFileUseCase, dispatchBridge)
    }

    val moveOperationHandler: MoveOperationHandler by lazy {
        MoveOperationHandler(useCaseModule.moveFileUseCase, dispatchBridge)
    }

    val deleteOperationHandler: DeleteOperationHandler by lazy {
        DeleteOperationHandler(useCaseModule.deleteFileUseCase, dispatchBridge)
    }

    val renameOperationHandler: RenameOperationHandler by lazy {
        RenameOperationHandler(useCaseModule.renameFileUseCase, dispatchBridge)
    }

    val createDirectoryOperationHandler: CreateDirectoryOperationHandler by lazy {
        CreateDirectoryOperationHandler(useCaseModule.createDirectoryUseCase, dispatchBridge)
    }

    fun createViewModelFactory(): ViewModelProvider.Factory {
        val baseFactory = DualPaneViewModelFactory(
            reducer = dualPaneReducer,
            refreshHandler = panelRefreshHandler,
            navigationHandler = panelNavigationHandler,
            copyHandler = copyOperationHandler,
            moveHandler = moveOperationHandler,
            deleteHandler = deleteOperationHandler,
            renameHandler = renameOperationHandler,
            createDirectoryHandler = createDirectoryOperationHandler
        )
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val vm = baseFactory.create(modelClass)
                if (vm is DualPaneViewModel) {
                    activeDispatcher = vm::dispatch
                }
                return vm
            }
        }
    }
}
