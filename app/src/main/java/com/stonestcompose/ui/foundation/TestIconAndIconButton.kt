package com.stonestcompose.ui.foundation

import com.stonestcompose.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Square
import androidx.compose.material.icons.outlined.Square
import androidx.compose.material.icons.rounded.Square
import androidx.compose.material.icons.sharp.Square
import androidx.compose.material.icons.twotone.Square
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stonestcompose.util.ToastUtil

// Icon 主要是用来加载矢量图的，但它更本质的特征是用来显示“单色符号”的
//       默认跟随 LocalContentColor 着色(tint)
@Preview(showBackground = true)
@Composable
fun TestIconAndIconButton() {
    InnerIcons()

    Load()
}

@Composable
private fun InnerIcons() {
    /*
    有五种不同的 Icons 图标主题：
    填充（Filled）、轮廓（Outlined）、圆角（Rounded）、双色（TwoTone）和尖锐（Sharp）。
    每个主题都包含相同的图标，但具有独特的视觉风格。
    通常选择一个主题并在整个应用程序中保持使用以确保一致性。
    例如，你可能想要使用一个属性或类型别名（typealias）来引用一个特定的主题，这样就可以从其他组合组件（composables）内部以语义上有意义的方式访问它。
     */
    LazyColumn {
        val icons = listOf(
            Icons.Default.Square, // 默认 就是 Filled 主题
            Icons.Filled.Square,
            Icons.Outlined.Square,
            Icons.Rounded.Square,
            Icons.TwoTone.Square,
            Icons.Sharp.Square
        )
        itemsIndexed(icons) { index, icon ->
            Icon(imageVector = icon, contentDescription = icon.name,
                modifier = Modifier.width(50.dp).height(50.dp))
        }
    }
}

@Composable
private fun Load() {
    Column(modifier = Modifier.padding(top = 300.dp)) {
        // 矢量xml资源 图
        Icon(
            imageVector = ImageVector.vectorResource(
                id = R.drawable.baseline_contact_mail_24
            ), "", tint = Color.Green,
            modifier = Modifier
                .width(88.dp)
                .height(88.dp),
        )

        Icon(
            bitmap = ImageBitmap.imageResource(
                id = R.mipmap.capsule_man
            ), contentDescription = "胶囊超人-位图",
            modifier = Modifier
                .width(100.dp)
                .height(100.dp),
            tint = Color.Unspecified
        ) // 不着色，否则就是一整块的填充色

        Icon(
            painter = painterResource(
                id = R.mipmap.camera
            ), contentDescription = "位图",
            modifier = Modifier
                .width(80.dp)
                .height(80.dp),
            tint = Color.Unspecified
        )
        IconButton(onClick = { }) {
            Icon(
                painter = painterResource(
                    id = R.drawable.baseline_contact_mail_24
                ), contentDescription = "矢量图",
                tint = Color.Blue
            )
        }


        MyIconButton(onClick = {
            ToastUtil.showToast("click it")
        }, Modifier.background(Color.Transparent)) {
            // AS 将.svg 导入步骤：
            // 1. 右键 res/drawable 文件夹
            // 2. New -> Vector Asset
            // 3. 选择 "Local file (SVG, PSD)" 导入 SVG 文件
            // 4. Android Studio 会自动转换为 XML 格式
            Icon(
                painter = painterResource(
                    id = R.drawable.exchange
                ), contentDescription = "矢量图",
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp),
                tint = Color.Unspecified
            )
        }
    }
}

// 复制源码中的 IconButton 实现，将 indication = 波纹指示效果 置为 null，
// 即 取消了 波纹点击效果
@Composable
fun MyIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null)
            .then(Modifier.size(120.dp)), // 增加了.then修饰
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
