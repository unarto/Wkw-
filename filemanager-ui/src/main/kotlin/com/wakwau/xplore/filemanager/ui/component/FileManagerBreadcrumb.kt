package com.wakwau.xplore.filemanager.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.wakwau.xplore.core.storage.model.StorageLocation
import com.wakwau.xplore.core.ui.components.BreadcrumbBar
import com.wakwau.xplore.core.ui.components.BreadcrumbItem
import com.wakwau.xplore.core.ui.theme.XploreCyan

@Composable
fun FileManagerBreadcrumb(
    location: StorageLocation?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = XploreCyan
) {
    val items = remember(location) {
        if (location == null) return@remember emptyList()
        val parts = location.path.split("/").filter { it.isNotEmpty() }
        val breadcrumbs = mutableListOf<BreadcrumbItem>()
        var currentPath = ""
        
        breadcrumbs.add(BreadcrumbItem(name = "Root", path = "/", isRoot = true))
        
        for (part in parts) {
            currentPath += "/$part"
            breadcrumbs.add(BreadcrumbItem(name = part, path = currentPath))
        }
        breadcrumbs
    }

    BreadcrumbBar(
        items = items,
        onItemClick = { onNavigate(it.path) },
        modifier = modifier,
        activeColor = activeColor
    )
}
