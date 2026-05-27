package com.stonestcompose.ui.layout

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stonestcompose.R
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Preview(showBackground = true)
@Composable
fun LazyColumnDemo() {
    // 创建滚动状态
    val scrollState = rememberScrollState()
    Column(Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {
        Box(Modifier
            .height(500.dp)
            .fillMaxWidth()
            .background(Color.Blue))
        Box(Modifier
            .height(800.dp)
            .fillMaxWidth()
            .background(Color.Red))
    }
}

@Preview(showBackground = true)
@Composable
fun LazyColumnCase() {
    // 延迟加载 垂直列表
    LazyColumn {
        // Add a single item
        item {
            Text(text = "First item")
        }

        // Add 5 items      项键:key
        items(5, key = { "item_$it" }) { index ->
            Text(text = "Item: $index")
        }

        // Add another single item
        item {
            Text(text = "Last item")
        }
    }

    val list = (101 .. 104).toList()
    LazyColumn(modifier = Modifier.padding(top = 150.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        // 内容间距
        verticalArrangement = Arrangement.spacedBy(4.dp)) {
        itemsIndexed(list) { index, item ->
            Text(text = "Item: $item at index $index",
                // 项动画
                modifier = Modifier.animateItem())
        }
    }

    LazyRow(
        modifier = Modifier.padding(top = 260.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        itemsIndexed(list) { index, item ->
            Text(text = "(Item: $item)")
        }
    }

    // 网格
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(top = 300.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(5) {
            Image(
                painter = painterResource(id = if (it % 2 == 0) R.mipmap.capsule_man else R.mipmap.star_cloud),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }
    }

    SegmentedButtonExample(Modifier.padding(top = 620.dp))
}

@Composable
fun SegmentedButtonExample(modifier: Modifier) {
    // 定义选中索引的状态
    var selectedIndex by remember { mutableIntStateOf(0) }
    // 定义选项列表
    val options = listOf("日视图", "周视图", "月视图")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "请选择时间跨度：", modifier = Modifier.padding(bottom = 8.dp))

        // 容器组件
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, label ->
                // 单个按钮组件
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex,
                    label = { Text(label) }
                )
            }
        }

        Text(
            text = "当前选中：${options[selectedIndex]}",
            modifier = Modifier.padding(top = 16.dp)
        )

        ListWithHeader(listOf(Item(1, "item 1"), Item(2, "item 2")))
    }
}

data class Item(val id: Int, val name: String)
// 粘性标题
@Composable
fun ListWithHeader(items: List<Item>) {
    LazyColumn {
        // 一个标题， 一个列表
        stickyHeader {
            // header composable
            Text("Header: AA", fontWeight = FontWeight.Bold, color = Color.Magenta)
        }

        items(items.size,
            // 相同 contentType 的 item 在滚动时会优先互相复用布局
            // Compose 会在渲染每一行时，调用 该 Lambda 表达式，复用(移出屏幕外的)计算结果一致的布局
            contentType = { it::class.java.simpleName } ) {
            // item row composable
            Text("item ${items[it].name}")
        }
    }
}

// 响应滚动位置
@Composable
fun MessageList(messages: List<Item>) {
    // Remember our own LazyListState
    val listState = rememberLazyListState()

    // Provide it to LazyColumn
    LazyColumn(state = listState) {
        // ...

        // 提供有关当前显示的所有列表项以及这些项在屏幕上的边界的信息
//        listState.layoutInfo
    }

    // 是否有可见项
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    // 默认，淡入淡出动画
    // 可以通过 enter 和 exit 参数来自定义过渡效果
    // 多种内置的过渡效果，如 slideInVertically、fadeIn、scaleIn 等，你可以使用 + 号将它们组合起来。
    //            enter: in,  exit: out
    AnimatedVisibility(visible = showButton, /*enter = fadeIn(), exit = fadeOut()*/) {
        // ScrollToTopButton()
    }

    // 控制滚动位置
    // listState.scrollToItem(index = 0)
    // listState.animateScrollToItem(index = 0) // 带有动画滚动，平滑滚动
}

// 将 Compose 的状态（State）转换为 Kotlin 的协程流（Flow）
@Composable
fun StateToFlow() {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> index > 0 } // 转换为布尔值：是否超过第一项
            .distinctUntilChanged()     // 只有当布尔值发生变化时才继续
            .collect { isScrolledPastFirstItem ->
                // 在这里执行副作用，比如更新另一个状态或打印日志
                println("是否滚动超过第一项: $isScrolledPastFirstItem")
            }
    }
}

// 借助 Paging 库，Paging 3.0 及更高版本通过 androidx.paging:paging-compose 库提供 Compose 支持。
// 如需显示分页内容列表，可以使用 collectAsLazyPagingItems() 扩展函数，
// 然后将返回的 LazyPagingItems 传入 LazyColumn 中的 items()。
// 与视图中的 Paging 支持类似，可以通过检查 item 是否为 null，在加载数据时显示占位符：
//@Composable
//fun MessageList(pager: Pager<Int, Message>) {
//    val lazyPagingItems = pager.flow.collectAsLazyPagingItems()
//
//    LazyColumn {
//        items(
//            lazyPagingItems.itemCount,
//            key = lazyPagingItems.itemKey { it.id }
//        ) { index ->
//            val message = lazyPagingItems[index]
//            if (message != null) {
//                MessageRow(message)
//            } else {
//                MessagePlaceholder()
//            }
//        }
//    }
//}
