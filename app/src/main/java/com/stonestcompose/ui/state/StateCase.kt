package com.stonestcompose.ui.state

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stonestcompose.ui.foundation.ArticleSimpleData

// 定义页面枚举或密封类
sealed class Screen {
    object List : Screen()
    data class Detail(val articleId: String) : Screen()
}

@Composable
fun StateCase() {
    // 维护当前的页面状态, 默认是 列表页面
    var currentScreen by remember { mutableStateOf<Screen>(Screen.List) }

    // 根据状态决定渲染哪个页面
    when (val screen = currentScreen) {
        is Screen.List -> {
            ArticleListScreen(
                onArticleClick = { id ->
                    // 切换状态，触发重组，UI 就会“跳”到详情页
                    currentScreen = Screen.Detail(id)
                }
            )
        }
        is Screen.Detail -> {
            ArticleScreen(
                articleId = screen.articleId,
                onBack = { currentScreen = Screen.List } // 提供返回逻辑
            )
        }
    }
}

// viewModel() 只适合 VM 提供了 无参构造方法，或仅有一个 SavedStateHandle 类型的参数的构造方法
@Composable
fun ArticleListScreen(viewModel: ArticleListViewModel = viewModel(), onArticleClick: (String) -> Unit) {
    // 先声明 监听状态变化
    val list by viewModel.articleList.collectAsStateWithLifecycle()
    // 获取 文章列表
    viewModel.getArticleList()

    LazyColumn(modifier = Modifier.padding(top = 100.dp)) { // 列表
        items(list.size) { item ->
            Box(modifier = Modifier.clickable {
                onArticleClick(list[item].id)
            }) {
                Text(list[item].content ?: "",
                    Modifier.height(200.dp).padding(start = 80.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ArticleScreen(listViewModel: ArticleListViewModel = viewModel(),
                  detailViewModel: ArticleViewModel = viewModel(),
                  articleId: String, onBack: () -> Unit) {
    // 观察业务逻辑状态
    val article by detailViewModel.articleContent.collectAsStateWithLifecycle()

    // rememberSaveable 负责纯 UI 的状态（比如用户是否点击了“点赞”的临时动画状态，或者阅读到的滚动位置; 这里表示缩放状态）
    var isImageZoomed by rememberSaveable { mutableStateOf(false) }

    // 动画：当 isImageZoomed 改变时，scale 会在 1f 和 2.5f 之间平滑过渡
    val scale by animateFloatAsState(
        targetValue = if (isImageZoomed) 2.5f else 1f,
        // 自定义动画规格（增加一点回弹效果）
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "ImageZoomAnimation"
    )

    // 文章详情
    Column(modifier = Modifier.padding(top = 100.dp)) {
        Text(article?.content ?: "no data",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                // 图层缩放，跳过布局阶段，只在绘制阶段生效
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale
                )
                // 点击后 缩放
                .clickable { isImageZoomed = !isImageZoomed }
        )
        Button(
            modifier = Modifier.padding(top = 40.dp).align(Alignment.CenterHorizontally),
            onClick = onBack
        ) {
            Text("Back to List Page")
        }
    }

    // LaunchedEffect 会在 Composable 首次显示或 articleId 改变时执行
    LaunchedEffect(articleId) {
        detailViewModel.updateArticleId(articleId)
        detailViewModel.updateArticle(listViewModel.articleList.value.first { it.id == articleId })
    }

}