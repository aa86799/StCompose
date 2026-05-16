package com.stonestcompose.ui.gesture

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview(showBackground = true)
fun PointerInputCase() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Color.LightGray)
            // 处理底层手势输入的核心修饰符 pointerInput
            // 提供了一个可以访问原始指针事件（如触摸、鼠标点击、鼠标滚动等）的协程作用域
            .pointerInput(Unit) { // Unit 表示这个协程在 Box 生命周期内只运行一次
                detectTapGestures(
                    onPress = { /* 触摸开始时触发 */
                        Log.i("detectTapGestures#onPress", "$it")
                    },
                    onDoubleTap = { /* 双击 */
                        Log.i("detectTapGestures#onDoubleTap", "$it")
                    },
                    onLongPress = { /* 长按 */
                        Log.i("detectTapGestures#onLongPress", "$it")
                    },
                    onTap = { /* 普通点击 */
                        Log.i("detectTapGestures#onTap", "$it")
                    }
                )
            }
    )
}