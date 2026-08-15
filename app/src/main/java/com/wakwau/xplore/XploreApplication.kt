package com.wakwau.xplore

import android.app.Application
import com.wakwau.xplore.preferences.MMKVPreferenceStore

class XploreApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MMKVPreferenceStore.initialize(this)
    }
}
