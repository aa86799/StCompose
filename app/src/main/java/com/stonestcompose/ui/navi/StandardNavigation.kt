package com.stonestcompose.ui.navi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stonestcompose.ui.foundation.TestButton
import com.stonestcompose.ui.foundation.TestIconAndIconButton
import com.stonestcompose.ui.foundation.TestText
import com.stonestcompose.ui.foundation.TestTextField
import com.stonestcompose.ui.navi.NaviArgsRoute
import com.stonestcompose.ui.state.StateCase



@Composable
@Preview(showBackground = true)
fun StandardNavigation() {
    // 创建并记住 NavController，它是导航的控制器
    val navController = rememberNavController()

    // NavHost 是导航的容器，负责渲染当前的页面
    // startDestination 指定了应用启动时的第一个页面 (此处使用路由对象)
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        // 配置首页的 Composable
        // composable<T>() 是泛型函数，T 是路由类型
        composable<HomeRoute> {
            HomeScreen(navController = navController)
        }

        // 带有关闭按钮
        closableComposable<StateCaseRoute>(navController) { backStackEntry, t ->
            StateCase()
        }
        // 普通页面
//        composable<StateCaseRoute> {
//            StateCase()
//        }

        closableComposable<NaviArgsRoute>(navController) { backStackEntry, t ->
            NaviArgsScreen(navController, backStackEntry, t.articleId)
        }
        closableComposable<NaviEditRoute>(navController) { backStackEntry, t ->
            NaviEditScreen(navController, backStackEntry, t.articleId)
        }

        composable<TestTextRoute> {
            TestText()
        }

        composable<TestButtonRoute> {
            TestButton()
        }

        composable<TestTextFieldRoute> {
            TestTextField()
        }

        composable<TestIconAndIconButtonRoute> {
            TestIconAndIconButton()
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
//  Text(text = "Home Screen")
//  垂直排列：一个 标题，一个列表
    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        Text("功能仪表盘", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
        // 动态纵向列表
        LazyColumn {
            itemsIndexed(Menu.TITLES) { index, title ->
                NaviItem(title) {
                    val route = when (title) {
                        "test StateCase" -> StateCaseRoute
                        "test NaviArgsRoute" -> NaviArgsRoute(121)
                        "test FD.Text" -> TestTextRoute
                        "test FD.Button" -> TestButtonRoute
                        "test FD.TextField" -> TestTextFieldRoute
                        "test FD.Icon/IconButton" -> TestIconAndIconButtonRoute
                        else -> null
                    }
                    route?.let {
                        navController.navigate(route)
                    }
                }
            }
        }
    }
}

@Composable
private fun NaviItem(title: String, onClick: () -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .height(80.dp)
        .clickable(onClick = onClick)) {
        Text(title, modifier = Modifier.padding(16.dp))
    }
}


