package com.wakwau.xplore.filemanager.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.wakwau.xplore.core.ui.theme.DarkSurface
import com.wakwau.xplore.core.ui.theme.TextPrimaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileManagerTopBar(
    title: String = "WKW Xplore",
    onSearchClick: () -> Unit = {},
    onOverflowClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { 
            Text(
                text = title, 
                color = TextPrimaryDark 
            ) 
        },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkSurface
        ),
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextPrimaryDark
                )
            }
            IconButton(onClick = onOverflowClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = TextPrimaryDark
                )
            }
        }
    )
}
