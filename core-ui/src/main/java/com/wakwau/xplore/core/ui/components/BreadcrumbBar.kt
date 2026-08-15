package com.wakwau.xplore.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.SdStorage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wakwau.xplore.core.ui.theme.DarkBorder
import com.wakwau.xplore.core.ui.theme.DarkSurfaceHighlight
import com.wakwau.xplore.core.ui.theme.XploreCyan
import com.wakwau.xplore.core.ui.theme.XploreOrange

data class BreadcrumbItem(
    val name: String,
    val path: String,
    val isRoot: Boolean = false
)

@Composable
fun BreadcrumbBar(
    items: List<BreadcrumbItem>,
    onItemClick: (BreadcrumbItem) -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = XploreCyan
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(items.size) {
        if (items.isNotEmpty()) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(34.dp)
            .background(Color(0xFF1B232E))
            .horizontalScroll(scrollState)
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            val isLast = index == items.lastIndex
            val itemBackground = if (isLast) activeColor.copy(alpha = 0.25f) else DarkSurfaceHighlight

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(itemBackground)
                    .clickable { onItemClick(item) }
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.isRoot) {
                    Icon(
                        imageVector = Icons.Default.SdStorage,
                        contentDescription = "Root",
                        tint = if (isLast) activeColor else Color(0xFF90CAF9),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Text(
                    text = item.name,
                    fontSize = 11.sp,
                    fontWeight = if (isLast) FontWeight.Bold else FontWeight.Normal,
                    color = if (isLast) activeColor else Color(0xFFECEFF1),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (!isLast) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFF607D8B),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
