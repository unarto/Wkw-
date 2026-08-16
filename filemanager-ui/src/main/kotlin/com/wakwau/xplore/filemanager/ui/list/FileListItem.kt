package com.wakwau.xplore.filemanager.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wakwau.xplore.core.storage.model.FileItem
import com.wakwau.xplore.core.storage.model.FileType
import com.wakwau.xplore.core.ui.components.FileIcon
import com.wakwau.xplore.core.ui.theme.DarkBorder
import com.wakwau.xplore.core.ui.theme.DarkSurfaceElevated
import com.wakwau.xplore.core.ui.theme.DarkSurfaceHighlight
import com.wakwau.xplore.core.ui.theme.TextPrimaryDark
import com.wakwau.xplore.core.ui.theme.TextSecondaryDark
import com.wakwau.xplore.core.ui.theme.XploreOrange
import com.wakwau.xplore.core.util.ByteFormatter
import com.wakwau.xplore.core.util.DateFormatter
import com.wakwau.xplore.core.util.MimeTypeDetector

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileListItem(
    item: FileItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onCheckToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDir = item.type == FileType.DIRECTORY
    val bg = if (isSelected) DarkSurfaceHighlight else Color.Transparent
    val ext = item.name.substringAfterLast('.', "")
    val category = MimeTypeDetector.getCategory(item.name, isDir)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(bg)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isDir) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondaryDark,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(16.dp))
        }
        Spacer(modifier = Modifier.width(4.dp))
        FileIcon(category = category, isDirectory = isDir, extension = ext, size = 26.dp)
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                color = if (isSelected) XploreOrange else TextPrimaryDark,
                fontWeight = if (isDir) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val subText = if (isDir) {
                DateFormatter.formatShort(item.metadata.modifiedTime)
            } else {
                "${DateFormatter.formatShort(item.metadata.modifiedTime)}  •  ${ByteFormatter.format(item.metadata.size)}"
            }
            Text(text = subText, color = TextSecondaryDark, fontSize = 11.sp)
        }
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onCheckToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = XploreOrange,
                checkmarkColor = Color.Black,
                uncheckedColor = DarkBorder
            ),
            modifier = Modifier.size(28.dp)
        )
    }
}
