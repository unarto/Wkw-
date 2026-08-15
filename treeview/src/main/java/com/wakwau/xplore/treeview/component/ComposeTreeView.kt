package com.wakwau.xplore.treeview.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.wakwau.xplore.treeview.interaction.TreeInteraction
import com.wakwau.xplore.treeview.model.TreeNode
import com.wakwau.xplore.treeview.state.TreeState

@Composable
fun <T> ComposeTreeView(
    treeState: TreeState<T>,
    modifier: Modifier = Modifier,
    interaction: TreeInteraction<T>? = null,
    nodeContent: @Composable (node: TreeNode<T>) -> Unit
) {
    val visibleNodes by treeState.visibleNodes.collectAsState()

    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = visibleNodes,
            key = { it.node.id }
        ) { flattenedNode ->
            TreeNodeRow(
                flattenedNode = flattenedNode,
                onToggle = {
                    treeState.toggle(flattenedNode.node)
                    interaction?.onToggle(flattenedNode.node)
                },
                onClick = {
                    interaction?.onNodeClick(flattenedNode.node)
                },
                onLongClick = {
                    interaction?.onNodeLongClick(flattenedNode.node)
                }
            ) { node ->
                nodeContent(node)
            }
        }
    }
}

