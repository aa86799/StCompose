package com.stonestcompose.ui.foundation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize

@Composable
@Preview(showBackground = true)
fun ModiGloballyPositioned() {
    var columnSize by remember { mutableStateOf(IntSize.Zero) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // 只要组件在屏幕上的全局位置变了（即使尺寸没变，只是父布局滚动了），它都会触发
            /*
            避免引发死循环：
如果在 onGloballyPositioned 的回调里直接修改一个会影响当前布局的 state（比如修改组件的宽度），
可能会触发重新布局，导致 onGloballyPositioned 再次触发，从而陷入死循环
             */
            .onGloballyPositioned { coordinates ->
                // 这里可以获取坐标和大小
                columnSize = coordinates.size
                // 相对于根布局或 window 的位置
//                val position = coordinates.positionInRoot()
                // 相对于整个屏幕的位置
                val position = coordinates.positionInWindow()
                // 相对于某个祖先布局的位置
//                val position = coordinates.localPositionOf(...)
                println("组件位置: $position, 尺寸: $columnSize")
            }
    ) {

    }
}