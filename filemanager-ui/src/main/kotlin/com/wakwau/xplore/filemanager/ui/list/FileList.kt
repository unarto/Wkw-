package com.wakwau.xplore.filemanager.ui.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wakwau.xplore.core.storage.model.FileItem
import com.wakwau.xplore.core.ui.theme.DarkBorder

@Composable
fun FileList(
    items: List<FileItem>,
    selectedItemIds: Set<String>,
    onItemClick: (FileItem) -> Unit,
    onItemLongClick: (FileItem) -> Unit,
    onItemCheckToggle: (FileItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            FileListItem(
                item = item,
                isSelected = selectedItemIds.contains(item.id),
                onClick = { onItemClick(item) },
                onLongClick = { onItemLongClick(item) },
                onCheckToggle = { onItemCheckToggle(item) }
            )
            HorizontalDivider(color = DarkBorder.copy(alpha = 0.4f), thickness = 0.5.dp)
        }
    }
}
