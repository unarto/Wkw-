package com.wakwau.xplore

import android.app.Application
import com.wakwau.xplore.di.AppCompositionRoot
import com.wakwau.xplore.preferences.MMKVPreferenceStore

class XploreApplication : Application() {
    lateinit var appCompositionRoot: AppCompositionRoot
        private set

    override fun onCreate() {
        super.onCreate()
        MMKVPreferenceStore.initialize(this)
        appCompositionRoot = AppCompositionRoot()
    }
}
