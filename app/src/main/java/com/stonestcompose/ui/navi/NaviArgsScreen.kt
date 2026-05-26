package com.stonestcompose.ui.navi

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.stonestcompose.vm.BaseViewModel
import kotlin.random.Random

@Composable
fun NaviArgsScreen(navController: NavController, backStackEntry: NavBackStackEntry, articleId: Int) {
    Column(modifier = Modifier.padding(top = 100.dp)) {
        Text("articleId=$articleId")


        // 在前一个页面读取：
        val result = backStackEntry
            ?.savedStateHandle
            ?.getStateFlow<String>("result_key", "")
            ?.collectAsState()
        if (!result?.value.isNullOrBlank()) {
            Text("result: ${result.value}", Modifier.height(200.dp))
        } else {
            Text("content", Modifier.height(200.dp))
        }

        Button(onClick = {
            navController.navigate(NaviEditRoute(articleId))
        }) {
            Text("click to Detail Page")
        }
    }
}

@Composable
fun NaviEditScreen(navController: NavController, backStackEntry: NavBackStackEntry, articleId: Int) {
//    val lifecycle = backStackEntry.lifecycle
//    lifecycle.coroutineScope

    // LocalLifecycleOwner.current 默认指向的就是当前的 NavBackStackEntry
    val lifecycleOwner = LocalLifecycleOwner.current
//    lifecycleOwner.lifecycle.coroutineScope

    // 确保当 Composable 离开屏幕（从组合树中移除）时，自动注销监听器，防止内存泄漏。
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> Log.d("NaviEditScreen","页面创建")
                Lifecycle.Event.ON_RESUME -> Log.d("NaviEditScreen","页面可见（进入前台）")
                Lifecycle.Event.ON_PAUSE -> Log.d("NaviEditScreen","页面暂停（被遮挡或离开）")
                Lifecycle.Event.ON_STOP -> Log.d("NaviEditScreen","页面停止")
                Lifecycle.Event.ON_DESTROY -> Log.d("NaviEditScreen","页面销毁")
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }

    }

    Column(modifier = Modifier.padding(top = 100.dp)) {
        Text("edit articleId=$articleId")
        Text("edit content", Modifier.height(200.dp))

        // NavBackStackEntry 实现了 ViewModelStoreOwner 接口。
        // 这意味着 ViewModel 的生命周期可以绑定到当前的路由条目上。
        // 此时 该 ViewModel 将随该页面的销毁而销毁。
        val vm:BaseViewModel = viewModel(backStackEntry)
        vm.test()

        Button(onClick = {

            // NavBackStackEntry 实现了 SavedStateRegistryOwner 接口，
            // 提供了在进程被杀掉（Process Death）后恢复数据的能力

            // 获取上一个页面的 entry
            val previousEntry = navController.previousBackStackEntry
            // 存入数据
            previousEntry?.savedStateHandle?.set("result_key",
                "newArticleObj_${Random.nextInt(100)}")
            navController.popBackStack()
        }) {
            Text("save to back")
        }
    }
}