package com.stonestcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.stonestcompose.ui.founddation.TestButton
import com.stonestcompose.ui.founddation.TestText
import com.stonestcompose.ui.founddation.TestTextField
import com.stonestcompose.ui.theme.StComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StComposeTheme {
                ShowMenu()
            }
        }
    }

    companion object {
        val TITLES = listOf(
            "test Text",
            "test Button",
            "test TextField",
            "test Layout/Measure",
            "test Pager",
            "test BottomSheet",
            "test BottomNavigation",
            "test Surface",
            "test Scaffold",
            "test FlowLayout",
            "test Column",
            "test Row",
            "test Box",
            "test Slider",
            "test Image",
            "test Icon",
            "test FAB",
            "test Card",
            "test Button",
            "test Text/Image/Button",
            "test Row/Column/Box",
            "test Material Design",
            "test RememberState LazyColumn",
            "test AlertDialog",
            "test ",
            "test ",
            "test ",
        )
    }
}

// 注意：要预览，必须是无参的 Composable 函数
@Preview(showBackground = true)
@Composable
private fun ShowMenu() {
    val menus = MainActivity.TITLES
    // 当前页面的状态
    val currentFullPage = remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        MainDashboard(menus) { page ->
            currentFullPage.value = page
        }

        // 使用 AnimatedContent 做出现/消失动画
        AnimatedContent(
            targetState = currentFullPage,
            label = "FullPageTransition",
            modifier = Modifier.fillMaxSize().zIndex(1f), // 确保覆盖在最上面
            transitionSpec = {
                // 定义动画：从下往上滑入，从上往下滑出
                slideInVertically(tween(durationMillis = 5000)) { height -> height } + fadeIn(tween(durationMillis = 1500)) togetherWith
                        slideOutVertically(tween(durationMillis = 5000)) { height -> -height } + fadeOut(tween(durationMillis = 1500))
            }
        ) { targetScreen ->

            if (targetScreen.value != "") {
                // 关键：拦截物理返回键，若是在屏幕左右边缘向里滑动退出，也能执行到
                // 当全屏页显示时，按返回键 = 关闭全屏页，而不是退出 App
                BackHandler {
                    currentFullPage.value = ""
                }

                // 渲染具体的全屏页面，且背景不透明
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background // 必须设置背景色遮挡底部
                ) {
                    // 自定义的工厂函数，加载不同的功能页面
                    clickToShowMenu(targetScreen.value)

                    // 在这里覆盖一个悬浮的 关闭按钮
                    Box(modifier = Modifier.fillMaxSize()) {
                        Button(
                            onClick = { currentFullPage.value = "" },
                            modifier = Modifier.align(Alignment.CenterEnd).padding(16.dp)
                        ) {
                            Text("关闭")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MainDashboard(menus: List<String>, onOpenPage: (String) -> Unit) {
    // 垂直排列：一个 标题，一个列表
    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        Text("功能仪表盘", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
        // 动态纵向列表
        LazyColumn {
            itemsIndexed(menus) { index, title ->
                DashboardButton(title) {
                    onOpenPage(title)
                }
            }
        }
    }
}

@Composable
private fun DashboardButton(title: String, onClick: () -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .height(80.dp)
        .clickable(onClick = onClick)) {
        Text(title, modifier = Modifier.padding(16.dp))
    }
}

@Composable
private fun clickToShowMenu(item: String) {
    when (item) {
        "test Text" -> TestText()
        "test Button" -> TestButton()
        "test TextField" -> TestTextField()
    }
}

