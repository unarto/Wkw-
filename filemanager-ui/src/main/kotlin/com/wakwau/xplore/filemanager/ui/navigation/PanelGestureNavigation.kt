package com.wakwau.xplore.filemanager.ui.navigation

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.wakwau.xplore.filemanager.ui.state.PanelId

@Composable
fun PanelGestureNavigation(
    activePanelId: PanelId,
    onSwipePanel: (PanelId) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var totalDrag by remember { mutableFloatStateOf(0f) }
    val threshold = 100f

    Box(
        modifier = modifier.pointerInput(activePanelId) {
            detectHorizontalDragGestures(
                onDragStart = { totalDrag = 0f },
                onDragEnd = {
                    if (totalDrag < -threshold && activePanelId == PanelId.LEFT) {
                        onSwipePanel(PanelId.RIGHT)
                    } else if (totalDrag > threshold && activePanelId == PanelId.RIGHT) {
                        onSwipePanel(PanelId.LEFT)
                    }
                    totalDrag = 0f
                },
                onDragCancel = { totalDrag = 0f },
                onHorizontalDrag = { _, dragAmount ->
                    totalDrag += dragAmount
                }
            )
        }
    ) {
        content()
    }
}
