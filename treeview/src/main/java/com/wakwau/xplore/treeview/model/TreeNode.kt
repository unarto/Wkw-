package com.wakwau.xplore.treeview.model

import java.util.UUID

class TreeNode<T>(
    val data: T,
    val id: String = UUID.randomUUID().toString()
) {
    var parent: TreeNode<T>? = null
        internal set

    private val _children = mutableListOf<TreeNode<T>>()
    val children: List<TreeNode<T>> get() = _children

    var isExpanded: Boolean = false
        private set

    val depth: Int
        get() {
            var d = 0
            var current = parent
            while (current != null) {
                d++
                current = current.parent
            }
            return d
        }

    val isRoot: Boolean get() = parent == null
    val hasChildren: Boolean get() = _children.isNotEmpty()

    fun addChild(child: TreeNode<T>) {
        var current = this as TreeNode<T>?
        while (current != null) {
            if (current.id == child.id) {
                throw IllegalArgumentException("Cycle detected: Cannot add node as its own descendant.")
            }
            current = current.parent
        }
        child.parent = this
        _children.add(child)
    }

    fun removeChild(child: TreeNode<T>) {
        if (_children.remove(child)) {
            child.parent = null
        }
    }

    fun clearChildren() {
        _children.forEach { it.parent = null }
        _children.clear()
    }

    fun expand() { isExpanded = true }
    fun collapse() { isExpanded = false }
    fun toggleExpanded() { isExpanded = !isExpanded }
}
