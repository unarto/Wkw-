package com.wakwau.xplore.treeview.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wakwau.xplore.core.ui.theme.DarkBorder

@Composable
fun TreeBranchGuide(
    depth: Int,
    isLastChild: Boolean = false,
    branchColor: Color = DarkBorder,
    indentWidth: Dp = 16.dp,
    modifier: Modifier = Modifier
) {
    if (depth <= 0) return

    Row(modifier = modifier) {
        repeat(depth) { index ->
            val isCurrentLevel = index == depth - 1

            Canvas(
                modifier = Modifier
                    .width(indentWidth)
                    .height(36.dp)
            ) {
                val midX = size.width / 2
                val midY = size.height / 2

                if (!isCurrentLevel) {
                    drawLine(
                        color = branchColor.copy(alpha = 0.35f),
                        start = Offset(midX, 0f),
                        end = Offset(midX, size.height),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f),
                        cap = StrokeCap.Round
                    )
                } else {
                    drawLine(
                        color = branchColor,
                        start = Offset(midX, 0f),
                        end = Offset(midX, if (isLastChild) midY else size.height),
                        strokeWidth = 1.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = branchColor,
                        start = Offset(midX, midY),
                        end = Offset(size.width, midY),
                        strokeWidth = 1.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}
