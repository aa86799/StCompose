package com.stonestcompose.ui.founddation

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun TestButton() {
    // 获取按钮的 交互状态
    val interactionState = remember { MutableInteractionSource() }
    // 使用 Kotlin 的解构方法
//        interactionState.collectIsPressedAsState() // 按压
//        interactionState.collectIsDraggedAsState() // 拖动
//        interactionState.collectIsFocusedAsState() // 焦点
//        interactionState.collectIsHoveredAsState() // 徘徊，悬停
    // 解构 不同交互状态时创建的对象
    val (text, textColor, buttonColor) = when {
        interactionState.collectIsPressedAsState().value -> ButtonState(
            "按下",
            Color.Red,
            Color.Black
        )

        else -> ButtonState("默认", Color.White, Color.Magenta)
    }
    Column {
        Text(text = "按钮按下和默认状态的文字颜色, 按钮颜色变化; \n" +
                "形状--圆角矩形;\n" +
                "内部文本Text 居中;\n" +
                "增加关闭按钮,",
            color = textColor,
            fontSize = 30.sp,
            modifier = Modifier.background(Color.Blue))
        Button(
            onClick = { /*TODO*/ },
            interactionSource = interactionState,
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(buttonColor),
//            modifier = Modifier.width(IntrinsicSize.Min).height(100.dp)
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .width(200.dp).height(200.dp)
                    .background(Color.Red)
            ) {
                Text(
                    text, color = textColor, fontSize = 40.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
//                    .width(200.dp).height(200.dp)
                        .background(Color.Green)
                        .align(Alignment.Center)
//                    .padding(20.dp)
//            modifier = Modifier.width(200.dp).height(120.dp)
//                .padding(10.dp).background(Color.Green)
                )
            }
            /*
             上面的 Text, 外层没有 Box时:
                先 background 再 padding, padding是有效的;
                而 先padding ,再background,  padding不生效
                align(CenterVertically) 的设置是无效的
             因 Text的宽高大小 默认就是 文本内容 的大小, 在外层增加 Box,
             Text 不指定宽高, 指定 align(Center) 有效的
             */
        }
    }
}

// 记录不同状态下按钮的样式
private data class ButtonState(var text: String, var textColor: Color, var buttonColor: Color)

