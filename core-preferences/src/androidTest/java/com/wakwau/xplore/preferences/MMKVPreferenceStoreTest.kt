package com.wakwau.xplore.preferences

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tencent.mmkv.MMKV
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MMKVPreferenceStoreTest {

    private lateinit val preferenceStore: MMKVPreferenceStore
    private lateinit val mmkv: MMKV

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        MMKV.initialize(context)
        mmkv = MMKV.mmkvWithID("test_prefs")!!
        preferenceStore = MMKVPreferenceStore(mmkv)
    }

    @After
    fun tearDown() {
        preferenceStore.clear()
    }

    @Test
    fun testStringOperations() {
        assertNull(preferenceStore.getString("key_str"))
        assertEquals("default", preferenceStore.getString("key_str", "default"))

        preferenceStore.putString("key_str", "value")
        assertEquals("value", preferenceStore.getString("key_str"))

        preferenceStore.putString("key_str", null)
        assertNull(preferenceStore.getString("key_str"))
    }

    @Test
    fun testBooleanOperations() {
        assertFalse(preferenceStore.getBoolean("key_bool"))
        assertTrue(preferenceStore.getBoolean("key_bool", true))

        preferenceStore.putBoolean("key_bool", true)
        assertTrue(preferenceStore.getBoolean("key_bool"))
    }

    @Test
    fun testIntOperations() {
        assertEquals(0, preferenceStore.getInt("key_int"))
        assertEquals(42, preferenceStore.getInt("key_int", 42))

        preferenceStore.putInt("key_int", 100)
        assertEquals(100, preferenceStore.getInt("key_int"))
    }

    @Test
    fun testLongOperations() {
        assertEquals(0L, preferenceStore.getLong("key_long"))
        assertEquals(42L, preferenceStore.getLong("key_long", 42L))

        preferenceStore.putLong("key_long", 100L)
        assertEquals(100L, preferenceStore.getLong("key_long"))
    }

    @Test
    fun testFloatOperations() {
        assertEquals(0f, preferenceStore.getFloat("key_float"))
        assertEquals(42f, preferenceStore.getFloat("key_float", 42f))

        preferenceStore.putFloat("key_float", 100f)
        assertEquals(100f, preferenceStore.getFloat("key_float"))
    }

    @Test
    fun testContainsAndRemove() {
        assertFalse(preferenceStore.contains("key_test"))
        
        preferenceStore.putString("key_test", "value")
        assertTrue(preferenceStore.contains("key_test"))

        preferenceStore.remove("key_test")
        assertFalse(preferenceStore.contains("key_test"))
    }

    @Test
    fun testClear() {
        preferenceStore.putString("key1", "val1")
        preferenceStore.putInt("key2", 2)
        
        assertTrue(preferenceStore.contains("key1"))
        assertTrue(preferenceStore.contains("key2"))

        preferenceStore.clear()

        assertFalse(preferenceStore.contains("key1"))
        assertFalse(preferenceStore.contains("key2"))
    }
}
