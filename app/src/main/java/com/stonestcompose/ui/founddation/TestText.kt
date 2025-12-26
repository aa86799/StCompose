package com.stonestcompose.ui.founddation

import androidx.compose.foundation.Image
import com.stonestcompose.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stonestcompose.util.ToastUtil
import kotlin.text.append
import kotlin.text.appendLine

@Preview(showBackground = true)
@Composable
fun TestText() {
    Column {
        Text("Hello World.  default")
        // 从 res 中加载文字
        Text("load app name from res: " + stringResource(id = R.string.app_name))
        Text(
            "标题格式",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            "style参数实现，字体大小、粗细、间距",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.W900,
                letterSpacing = 2.sp
            )
        )
        Text(
            "三个参数实现，字体大小、粗细、间距",
            fontSize = 20.sp,
            fontWeight = FontWeight.W900,
            letterSpacing = 2.sp
        )
        Text(
            text = "(测试 maxLines)桃花盛开，粉嫩的花瓣在春风中轻轻摇曳，展现出青春和生命的活力",
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "(测试 overflow=clip)桃花盛开，粉嫩的花瓣在春风中轻轻摇曳，展现出青春和生命的活力",
            maxLines = 1,
            overflow = TextOverflow.Clip,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "(测试 overflow=Ellipsis)桃花盛开，粉嫩的花瓣在春风中轻轻摇曳，展现出青春和生命的活力",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )
        // 文本行内溢出也展示所有，配合 Row+滚动，可见其效果
        val hScrollState = rememberScrollState()
        Row(modifier = Modifier.horizontalScroll(hScrollState)) {
            Text(
                text = "(测试 overflow=Visible，配合 横向滚动)桃花盛开，粉嫩的花瓣在春风中轻轻摇曳，展现出青春和生命的活力",
                maxLines = 1,
                overflow = TextOverflow.Visible,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            "设置了 fillMaxWidth() 之后，可以指定 Text 的对齐方式",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        ) // start/left, end/right
        Text("modifier 设置居中", modifier = Modifier.align(Alignment.CenterHorizontally))
        Text(
            "AA. lineHeight 设置每行的行高间距\nAA. 第二行",
            lineHeight = 30.sp
        )
        Text(
            "fontFamily 设置字体，内置字体",
            fontFamily = FontFamily.SansSerif
        )
        /*
         * 加载 res/font 下的字体：
         * 右键 res 文件夹，选择 Android Resource Directory -> 选择 font，创建;
         * 将 .ttf 字体 放入 res/font/
         */
        Text(
            "fontFamily 设置字体，外置字体",
            fontFamily = FontFamily(Font(R.font.originality, FontWeight.W400))
        )
        Text("添加点击动作", Modifier.clickable {
            ToastUtil.showToast("click 1")
        })
        Text(
            "添加点击动作，并取消点击的波纹", Modifier.clickable(
                interactionSource = null,
                indication = null,
                onClick = {
                    ToastUtil.showToast("click 2")
                },
            )
        )
        // AnnotatedString: 文本 + 格式化信息 + 语义标记
        Text(buildAnnotatedString {
            withStyle(
                SpanStyle(
                    fontSize = 20.sp, fontWeight = FontWeight.W900,
                    color = Color.Red, textDecoration = TextDecoration.Underline
                )
            ) {
                withStyle(ParagraphStyle(textAlign = TextAlign.Center)) {
                    append("AnnotatedString")
                }
            }
            appendLine("可实现不同的样式，比如粗体提醒，特殊颜色，下划线等")
        }, modifier = Modifier.clickable {
            ToastUtil.showToast("整体点击")
        })

        val urlText = "这里"
        val clickText = buildAnnotatedString {
            append("点击")
            pushStringAnnotation("url1", annotation = "https://www.baidu.com")
            withStyle(
                SpanStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(urlText)
            }
            pop() // 结束链接注解
            append("访问", "网站资源")
        }
        // ClickableText 过时了
        ClickableText(text = clickText) { offset ->
            clickText.getStringAnnotations(tag = "url1", offset, offset).firstOrNull()?.let {
                ToastUtil.showToast("点到了${it.item}")
            }
        }
        Text(text = clickText, modifier = Modifier.clickable {
            val start = clickText.text.indexOf(urlText)
            val end = start + urlText.length
            clickText.getStringAnnotations(tag = "url1", start, end).firstOrNull()?.let {
                ToastUtil.showToast("点到了${it.item}")
            }
        })
        ClickableText(
            text = buildAnnotatedString {
                appendLine("某个文字的点击示例：")
                append("勾选即代表同意")
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF0E9FF2),
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("用户协议")
                }
            },
            modifier = Modifier.padding(top = 10.dp),
            onClick = { offset ->
                ToastUtil.showToast("点击了第 $offset 位的文字")
            }
        )

        /*
         * 计算出两个汉字所占的宽高；
         * appendInlineContent 添加文本内容，注意其 id 值；
         * Text#inlineContent 定义的 组件 map，取出 对应 key替换 上一步对应id的位置
         */
        val textMeasurer = rememberTextMeasurer()
        val textSize = textMeasurer.measure(
            text = "谷歌",
            style = TextStyle(fontSize = 14.sp)
        ).size
        val textW = with(LocalDensity.current) {
            textSize.width.toSp()
        }
        val textH = with(LocalDensity.current) {
            textSize.height.toSp()
        }
        Text(
            text = buildAnnotatedString {
                append("多个局部点击示例：")
                append("你可以访问")
                withStyle(
                    SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    // 在文本流（Text Flow）中挖一个“坑”（占位符），
                    // 以便稍后在渲染时填入任意的 Composable 内容（通常是图标、图片或小标签）。
                    // alternateText 是给盲人读屏软件或复制文本时用的替代文字
                    appendInlineContent("link1", "谷歌aaa")
                }
                append("或者")
                withStyle(
                    SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    appendInlineContent("link2", "必应bbb")
                }
                append("搜索")
            },
            /*
             *InlineContent 是 Compose 中一个强大的功能，
             * 它允许在文本中插入自定义的可组合项（Composable），
             * 比如图标、按钮或其他自定义视图。这对于创建富文本内容特别有用
             *
             * Placeholder 中必须预先算好宽高，以容纳后面实际的 @composable 内容
             *          宽度要比计算出来的再宽一点，否则无法显示完全，不知是不是文字间距的原因
             */
            inlineContent = mapOf(
                "link1" to InlineTextContent(
                    Placeholder(
                        width = textW*1.2,
                        height = textH,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Top
                    )
                ) {
                    Text(
                        text = "谷歌",
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = ripple()
                        ) {
                            ToastUtil.showToast("点击了 谷歌")
                        }
                    )
                },
                "link2" to InlineTextContent(
                    Placeholder(
                        width = textW,
                        height = textH,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Top
                    )
                ) {
                    Icon(
                        painter  = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = null,
                        tint = Color.Blue,
                        modifier = Modifier.clickable {
                            ToastUtil.showToast("点击了 必应")
                        }
                    )
                }
            ), modifier = Modifier.padding(top = 10.dp)
        )

        SelectionContainer {
            Text("长按文字--选择复制 ".repeat(3))
        }

        /*
        CompositionLocalProvider  隐式地向下传递数据。
         */
        CompositionLocalProvider(LocalContentColor provides Color.Red) {
            Text("我是红色的，但我没设置 color 参数，继承了父域的颜色设置")
        }
        CompositionLocalProvider(LocalContentColor provides Color.Gray.copy(alpha = 0.5f)) {
            Text("我是半透明灰色的, 但我没设置 color 参数，继承了父域的颜色设置")
        }
    }
}