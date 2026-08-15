package com.wakwau.xplore.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wakwau.xplore.core.ui.theme.ApkColor
import com.wakwau.xplore.core.ui.theme.ArchiveColor
import com.wakwau.xplore.core.ui.theme.AudioColor
import com.wakwau.xplore.core.ui.theme.CodeColor
import com.wakwau.xplore.core.ui.theme.FolderColor
import com.wakwau.xplore.core.ui.theme.ImageColor
import com.wakwau.xplore.core.ui.theme.TextDocColor
import com.wakwau.xplore.core.ui.theme.VideoColor
import com.wakwau.xplore.core.util.FileCategory

@Composable
fun FileIcon(
    category: FileCategory,
    isDirectory: Boolean,
    isExpanded: Boolean = false,
    extension: String = "",
    size: Dp = 28.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        when {
            isDirectory -> {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.FolderOpen else Icons.Default.Folder,
                    contentDescription = "Folder",
                    tint = FolderColor,
                    modifier = Modifier.size(size)
                )
            }
            category == FileCategory.ARCHIVE -> {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.FolderZip,
                        contentDescription = "Archive",
                        tint = ArchiveColor,
                        modifier = Modifier.size(size)
                    )
                    // Small ZIP badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .clip(RoundedCornerShape(2.dp))
                            .background(ArchiveColor.copy(alpha = 0.9f))
                    ) {
                        Text(
                            text = "ZIP",
                            color = Color.Black,
                            fontSize = 6.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            category == FileCategory.APK -> {
                Icon(
                    imageVector = Icons.Default.Android,
                    contentDescription = "APK",
                    tint = ApkColor,
                    modifier = Modifier.size(size)
                )
            }
            category == FileCategory.IMAGE -> {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Image",
                    tint = ImageColor,
                    modifier = Modifier.size(size)
                )
            }
            category == FileCategory.AUDIO -> {
                Icon(
                    imageVector = Icons.Default.AudioFile,
                    contentDescription = "Audio",
                    tint = AudioColor,
                    modifier = Modifier.size(size)
                )
            }
            category == FileCategory.VIDEO -> {
                Icon(
                    imageVector = Icons.Default.VideoFile,
                    contentDescription = "Video",
                    tint = VideoColor,
                    modifier = Modifier.size(size)
                )
            }
            category == FileCategory.CODE -> {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = "Code",
                    tint = CodeColor,
                    modifier = Modifier.size(size)
                )
            }
            category == FileCategory.TEXT || category == FileCategory.DOCUMENT -> {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Document",
                    tint = TextDocColor,
                    modifier = Modifier.size(size)
                )
            }
            else -> {
                Icon(
                    imageVector = Icons.Default.InsertDriveFile,
                    contentDescription = "File",
                    tint = Color(0xFFB0BEC5),
                    modifier = Modifier.size(size)
                )
            }
        }
    }
}
