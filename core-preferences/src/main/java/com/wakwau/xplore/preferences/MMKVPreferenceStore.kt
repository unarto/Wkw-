package com.wakwau.xplore.preferences

import com.tencent.mmkv.MMKV

class MMKVPreferenceStore(
    private val mmkv: MMKV
) : PreferenceStore {

    override fun getString(key: String, defaultValue: String?): String? {
        return mmkv.decodeString(key, defaultValue)
    }

    override fun putString(key: String, value: String?) {
        if (value == null) {
            mmkv.removeValueForKey(key)
        } else {
            mmkv.encode(key, value)
        }
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mmkv.decodeBool(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return mmkv.decodeInt(key, defaultValue)
    }

    override fun putInt(key: String, value: Int) {
        mmkv.encode(key, value)
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return mmkv.decodeLong(key, defaultValue)
    }

    override fun putLong(key: String, value: Long) {
        mmkv.encode(key, value)
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return mmkv.decodeFloat(key, defaultValue)
    }

    override fun putFloat(key: String, value: Float) {
        mmkv.encode(key, value)
    }

    override fun contains(key: String): Boolean {
        return mmkv.containsKey(key)
    }

    override fun remove(key: String) {
        mmkv.removeValueForKey(key)
    }

    override fun clear() {
        mmkv.clearAll()
    }

    companion object {
        fun initialize(context: android.content.Context) {
            MMKV.initialize(context)
        }
    }
}
