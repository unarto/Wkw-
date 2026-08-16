package com.wakwau.xplore.di

import androidx.lifecycle.ViewModelProvider

class AppCompositionRoot {
    val storageModule = StorageModule()
    val fileManagerUseCaseModule = FileManagerUseCaseModule(
        directoryRepository = storageModule.directoryRepository,
        fileRepository = storageModule.fileRepository
    )
    val fileManagerPresentationModule = FileManagerPresentationModule(
        useCaseModule = fileManagerUseCaseModule
    )

    val dualPaneViewModelFactory: ViewModelProvider.Factory by lazy {
        fileManagerPresentationModule.createViewModelFactory()
    }
}
