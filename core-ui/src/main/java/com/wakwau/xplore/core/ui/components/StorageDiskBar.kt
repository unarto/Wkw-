package com.wakwau.xplore.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.SdCard
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wakwau.xplore.core.ui.theme.DarkSurfaceElevated
import com.wakwau.xplore.core.ui.theme.TextSecondaryDark
import com.wakwau.xplore.core.ui.theme.XploreCyan
import com.wakwau.xplore.core.ui.theme.XploreOrange

@Composable
fun StorageDiskBar(
    name: String,
    path: String,
    subFoldersCount: Int,
    subFilesCount: Int,
    freeSpaceText: String,
    totalSpaceText: String,
    usedPercentage: Float,
    isExternal: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkSurfaceElevated)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Storage Icon
        Icon(
            imageVector = if (isExternal) Icons.Default.SdCard else Icons.Default.PhoneAndroid,
            contentDescription = "Storage",
            tint = if (isExternal) XploreOrange else XploreCyan,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Info (Name, folder/file count, path)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = null,
                    tint = Color(0xFFFFB74D),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = " $subFoldersCount  ",
                    fontSize = 11.sp,
                    color = TextSecondaryDark
                )
                Icon(
                    imageVector = Icons.Default.InsertDriveFile,
                    contentDescription = null,
                    tint = Color(0xFF90CAF9),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = " $subFilesCount  ",
                    fontSize = 11.sp,
                    color = TextSecondaryDark
                )
                Text(
                    text = path,
                    fontSize = 10.sp,
                    color = TextSecondaryDark.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Free / Total Space Meter
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bebas ",
                    fontSize = 9.sp,
                    color = TextSecondaryDark
                )
                Text(
                    text = "$freeSpaceText / $totalSpaceText",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = XploreCyan
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF37474F))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(usedPercentage.coerceIn(0f, 1f))
                        .height(4.dp)
                        .background(
                            if (usedPercentage > 0.9f) Color(0xFFE53935)
                            else if (usedPercentage > 0.75f) XploreOrange
                            else XploreCyan
                        )
                )
            }
        }
    }
}
