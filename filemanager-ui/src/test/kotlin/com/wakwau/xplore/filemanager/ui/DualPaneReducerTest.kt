package com.wakwau.xplore.filemanager.ui

import com.wakwau.xplore.core.storage.model.FileItem
import com.wakwau.xplore.core.storage.model.FileMetadata
import com.wakwau.xplore.core.storage.model.FileType
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.filemanager.ui.event.DualPaneEvent
import com.wakwau.xplore.filemanager.ui.reducer.DualPaneReducer
import com.wakwau.xplore.filemanager.ui.state.DualPaneState
import com.wakwau.xplore.filemanager.ui.state.OperationUiState
import com.wakwau.xplore.filemanager.ui.state.PanelId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DualPaneReducerTest {

    private val reducer = DualPaneReducer()

    @Test
    fun `test active panel switching`() {
        val initial = DualPaneState(activePanelId = PanelId.LEFT)
        val switched = reducer.reduce(initial, DualPaneEvent.SetActivePanel(PanelId.RIGHT))
        assertEquals(PanelId.RIGHT, switched.activePanelId)
    }

    @Test
    fun `test selection LEFT does not affect RIGHT`() {
        val initial = DualPaneState()
        val updated = reducer.reduce(initial, DualPaneEvent.SelectItem(PanelId.LEFT, "file1"))
        
        assertTrue(updated.leftPanel.selectedItemIds.contains("file1"))
        assertTrue(updated.rightPanel.selectedItemIds.isEmpty())
    }

    @Test
    fun `test selection RIGHT does not affect LEFT`() {
        val initial = DualPaneState()
        val updated = reducer.reduce(initial, DualPaneEvent.SelectItem(PanelId.RIGHT, "file2"))
        
        assertTrue(updated.rightPanel.selectedItemIds.contains("file2"))
        assertTrue(updated.leftPanel.selectedItemIds.isEmpty())
    }

    @Test
    fun `test navigation LEFT does not affect RIGHT location`() {
        val initial = DualPaneState()
        val leftLoc = StorageLocation("/left/dir", "root1")
        val updated = reducer.reduce(initial, DualPaneEvent.OpenLocation(PanelId.LEFT, leftLoc))
        
        assertEquals(leftLoc, updated.leftPanel.currentLocation)
        assertEquals(null, updated.rightPanel.currentLocation)
    }

    @Test
    fun `test directory loaded updates items and resets loading`() {
        val initial = DualPaneState()
        val loc = StorageLocation("/path", "root")
        val items = listOf(
            FileItem(
                id = "/path/test.txt",
                name = "test.txt",
                location = StorageLocation("/path/test.txt", "root"),
                type = FileType.FILE,
                metadata = FileMetadata(
                    size = 0L,
                    modifiedTime = 0L,
                    createdTime = null,
                    isReadable = true,
                    isWritable = true,
                    isExecutable = false,
                    isHidden = false
                )
            )
        )
        val updated = reducer.reduce(initial, DualPaneEvent.DirectoryLoaded(PanelId.LEFT, loc, items))
        
        assertEquals(loc, updated.leftPanel.currentLocation)
        assertEquals(1, updated.leftPanel.items.size)
        assertFalse(updated.leftPanel.isLoading)
    }

    @Test
    fun `test operation cancelled updates operation state to Cancelled`() {
        val initial = DualPaneState(operationState = OperationUiState.Running("Copy"))
        val updated = reducer.reduce(initial, DualPaneEvent.OperationCancelled)
        
        assertTrue(updated.operationState is OperationUiState.Cancelled)
    }

    @Test
    fun `test operation failure does not corrupt panel items or location`() {
        val loc = StorageLocation("/test", "root")
        val initial = DualPaneState(
            operationState = OperationUiState.Running("Delete")
        )
        val withLoc = reducer.reduce(initial, DualPaneEvent.OpenLocation(PanelId.LEFT, loc))
        val failed = reducer.reduce(withLoc, DualPaneEvent.OperationFailed("IO_ERROR"))
        
        assertEquals(loc, failed.leftPanel.currentLocation)
        assertTrue(failed.operationState is OperationUiState.Failure)
        assertEquals("IO_ERROR", (failed.operationState as OperationUiState.Failure).errorMessage)
    }
}
