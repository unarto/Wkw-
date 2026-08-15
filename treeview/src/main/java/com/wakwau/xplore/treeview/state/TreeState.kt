package com.wakwau.xplore.treeview.state

import com.wakwau.xplore.treeview.model.FlattenedTreeNode
import com.wakwau.xplore.treeview.model.TreeNode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TreeState<T> {
    private val _roots = mutableListOf<TreeNode<T>>()
    val roots: List<TreeNode<T>> get() = _roots

    private val _visibleNodes = MutableStateFlow<List<FlattenedTreeNode<T>>>(emptyList())
    val visibleNodes: StateFlow<List<FlattenedTreeNode<T>>> = _visibleNodes.asStateFlow()

    fun setRoots(newRoots: List<TreeNode<T>>) {
        _roots.clear()
        _roots.addAll(newRoots)
        updateVisibleNodes()
    }

    fun clear() {
        _roots.clear()
        updateVisibleNodes()
    }

    fun expand(node: TreeNode<T>) {
        node.expand()
        updateVisibleNodes()
    }

    fun collapse(node: TreeNode<T>) {
        node.collapse()
        updateVisibleNodes()
    }

    fun toggle(node: TreeNode<T>) {
        node.toggleExpanded()
        updateVisibleNodes()
    }

    fun isExpanded(node: TreeNode<T>): Boolean {
        return node.isExpanded
    }

    private fun updateVisibleNodes() {
        val flatList = mutableListOf<FlattenedTreeNode<T>>()
        for ((index, root) in _roots.withIndex()) {
            flatten(root, flatList, isLastChild = index == _roots.lastIndex)
        }
        _visibleNodes.value = flatList
    }

    private fun flatten(node: TreeNode<T>, result: MutableList<FlattenedTreeNode<T>>, isLastChild: Boolean) {
        result.add(FlattenedTreeNode(node, node.depth, isLastChild))
        if (node.isExpanded) {
            val children = node.children
            for ((index, child) in children.withIndex()) {
                flatten(child, result, isLastChild = index == children.lastIndex)
            }
        }
    }
}
