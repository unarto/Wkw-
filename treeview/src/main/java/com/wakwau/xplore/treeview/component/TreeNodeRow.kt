package com.wakwau.xplore.treeview.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wakwau.xplore.treeview.model.FlattenedTreeNode
import com.wakwau.xplore.treeview.model.TreeNode

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> TreeNodeRow(
    flattenedNode: FlattenedTreeNode<T>,
    onToggle: () -> Unit,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    indentWidth: Dp = 16.dp,
    minHeight: Dp = 40.dp,
    content: @Composable (node: TreeNode<T>) -> Unit
) {
    val node = flattenedNode.node
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = minHeight)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TreeBranchGuide(
            depth = flattenedNode.depth,
            isLastChild = flattenedNode.isLastChild,
            indentWidth = indentWidth
        )
        TreeExpandToggle(
            isExpanded = node.isExpanded,
            isLeaf = !node.hasChildren,
            onToggle = onToggle
        )
        content(node)
    }
}

