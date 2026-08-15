package com.wakwau.xplore.preferences

interface PreferenceStore {
    fun getString(key: String, defaultValue: String? = null): String?
    fun putString(key: String, value: String?)

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun putBoolean(key: String, value: Boolean)

    fun getInt(key: String, defaultValue: Int = 0): Int
    fun putInt(key: String, value: Int)

    fun getLong(key: String, defaultValue: Long = 0L): Long
    fun putLong(key: String, value: Long)

    fun getFloat(key: String, defaultValue: Float = 0f): Float
    fun putFloat(key: String, value: Float)

    fun contains(key: String): Boolean
    fun remove(key: String)
    fun clear()
}
