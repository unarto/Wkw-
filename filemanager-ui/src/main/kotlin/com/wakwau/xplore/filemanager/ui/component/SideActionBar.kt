package com.wakwau.xplore.filemanager.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wakwau.xplore.core.ui.theme.DarkBorder
import com.wakwau.xplore.core.ui.theme.DarkSurface
import com.wakwau.xplore.core.ui.theme.TextPrimaryDark
import com.wakwau.xplore.core.ui.theme.TextSecondaryDark
import com.wakwau.xplore.core.ui.theme.XploreCyan
import com.wakwau.xplore.core.ui.theme.XploreGreen
import com.wakwau.xplore.core.ui.theme.XploreOrange
import com.wakwau.xplore.core.ui.theme.XploreRed

enum class SideAction {
    INFO, DISK_MAP, RENAME, COPY, PASTE, CREATE_ZIP, DELETE, NEW_FOLDER, SERVER_WIFI, SERVER_FTP, NEW_FILE
}

@Composable
fun SideActionBar(
    onActionClick: (SideAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxHeight()
            .width(68.dp)
            .background(DarkSurface)
    ) {
        Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(DarkBorder))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SideActionButton("Info", Icons.Default.Info, XploreCyan) { onActionClick(SideAction.INFO) }
            SideActionButton("Peta disk", Icons.Default.PieChart, XploreGreen) { onActionClick(SideAction.DISK_MAP) }
            SideActionButton("Ganti nama", Icons.Default.DriveFileRenameOutline, XploreCyan) { onActionClick(SideAction.RENAME) }
            SideActionButton("Salin", Icons.Default.ContentCopy, XploreOrange) { onActionClick(SideAction.COPY) }
            SideActionButton("Papan klip", Icons.Default.ContentPaste, XploreOrange) { onActionClick(SideAction.PASTE) }
            SideActionButton("Buat ZIP", Icons.Default.FolderZip, XploreCyan) { onActionClick(SideAction.CREATE_ZIP) }
            SideActionButton("Hapus", Icons.Default.Delete, XploreRed) { onActionClick(SideAction.DELETE) }
            SideActionButton("Folder baru", Icons.Default.CreateNewFolder, XploreOrange) { onActionClick(SideAction.NEW_FOLDER) }
            SideActionButton("Server WiFi", Icons.Default.Wifi, XploreCyan) { onActionClick(SideAction.SERVER_WIFI) }
            SideActionButton("Server FTP", Icons.Default.Dns, XploreGreen) { onActionClick(SideAction.SERVER_FTP) }
            SideActionButton("File teks baru", Icons.Default.NoteAdd, XploreCyan) { onActionClick(SideAction.NEW_FILE) }
        }
    }
}

@Composable
private fun SideActionButton(
    label: String,
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = tint, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, color = TextSecondaryDark, fontSize = 9.sp, textAlign = TextAlign.Center, maxLines = 1)
    }
}
