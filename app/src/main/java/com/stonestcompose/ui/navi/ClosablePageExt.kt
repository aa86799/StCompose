package com.stonestcompose.ui.navi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

// 定义包装容器
@Composable
fun ClosableWrapper(onClose: () -> Unit, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()

        Button(
            onClick = { onClose() },
            modifier = Modifier.align(Alignment.CenterEnd).padding(16.dp)
        ) {
            Text("关闭")
        }
    }
}

// 扩展 NavGraphBuilder
inline fun <reified T : Any> NavGraphBuilder.closableComposable(
    navController: NavHostController,
    noinline content: @Composable (NavBackStackEntry, T) -> Unit) {
    composable<T> { backStackEntry ->
        // 获取 路由类
        val routeInstance = backStackEntry.toRoute<T>()
        ClosableWrapper(onClose = { navController.popBackStack() }) {
            content(backStackEntry, routeInstance)
        }
    }
}