package com.wakwau.xplore.filemanager.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wakwau.xplore.core.ui.theme.DarkSurface
import com.wakwau.xplore.core.ui.theme.DarkSurfaceElevated
import com.wakwau.xplore.core.ui.theme.TextPrimaryDark
import com.wakwau.xplore.core.ui.theme.TextSecondaryDark
import com.wakwau.xplore.core.ui.theme.XploreBlue
import com.wakwau.xplore.core.ui.theme.XploreOrange
import com.wakwau.xplore.filemanager.ui.state.PanelId

@Composable
fun PanelSwitcher(
    activePanelId: PanelId,
    onSelectPanel: (PanelId) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(DarkSurface)
    ) {
        PanelTab(
            title = "Panel Kiri",
            isActive = activePanelId == PanelId.LEFT,
            activeColor = XploreBlue,
            onClick = { onSelectPanel(PanelId.LEFT) },
            modifier = Modifier.weight(1f)
        )
        PanelTab(
            title = "Panel Kanan",
            isActive = activePanelId == PanelId.RIGHT,
            activeColor = XploreOrange,
            onClick = { onSelectPanel(PanelId.RIGHT) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PanelTab(
    title: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (isActive) activeColor else DarkSurfaceElevated
    val textColor = if (isActive) Color.White else TextSecondaryDark
    Box(
        modifier = modifier
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .background(bg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
        )
    }
}
