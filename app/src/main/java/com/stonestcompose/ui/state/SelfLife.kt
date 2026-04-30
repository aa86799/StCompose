package com.stonestcompose.ui.state

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.RetainObserver
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// RememberObserver 和 RetainObserver 它们允许普通对象感知自身在 UI树 中的生命周期（何时被记住、何时被丢弃）。

// 定义核心业务类
class VideoPlayerManager {
    fun initialize() {
        Log.i("VideoPlayer", "VideoPlayer: 开始加载视频并准备播放...")
    }

    fun release() {
        Log.i("VideoPlayer", "VideoPlayer: 停止播放，释放解码器和内存...")
    }

    fun play() {
        Log.i("VideoPlayer", "VideoPlayer: 开始播放...")
    }
}

// 提供一个专门的 Composable 函数来获取和管理这个对象
@Composable
fun rememberVideoPlayerManager(): VideoPlayerManager {
    // 不要公开暴露实现了 RememberObserver 的类
    // 匿名内部类，隐藏 RememberObserver 的实现细节
    return remember {
        object : RememberObserver {
            val manager = VideoPlayerManager()

            // 成功进入 Compose 树 时
            override fun onRemembered() {
                manager.initialize()
            }

            // 正常离开 Compose 树 时
            override fun onForgotten() {
                manager.release()
            }

            // 重组被取消，根本没进入树时
            override fun onAbandoned() {
                // 如果 manager 的构造函数里分配了需要释放的资源，在这里释放。
                // 如果只是简单的对象创建，通常不需要做什么。
                Log.i("VideoPlayer", "onAbandoned")
            }
        }
    }.manager // 只把普通的 Manager 返回给外界
}

/* 协程生命周期管理 */
// 业务类中 使用协程
class MyDataController {
    // 创建一个受控的 Job
    private var job: Job? = null
    // 使用主线程调度器创建作用域
    private var coroutineScope: CoroutineScope? = null

    fun onStart() {
        job = Job()
        coroutineScope = CoroutineScope(Dispatchers.Main + job!!)

        // 在 onRemembered (对应这里的 onStart) 中启动协程
        coroutineScope?.launch {
            Log.i("controller", "onStart loadData")
            loadData()
        }
        Log.i("controller", "onStart")
    }

    fun onStop() {
        // 在 onForgotten 或 onAbandoned 中取消协程
        job?.cancel()
        job = null
        coroutineScope = null
        Log.i("controller", "onStop")
    }

    fun test() {
        Log.i("controller", "test")
    }

    private suspend fun loadData() {
        // 模拟网络请求
        delay(2000)
        Log.i("controller", "数据加载完成")
    }
}

@Composable
fun rememberDataController(): MyDataController {
    return remember {
        object: RememberObserver {
            val controller = MyDataController()
            override fun onRemembered() { // 进入
                controller.onStart()
            }

            override fun onForgotten() { // 离开
                controller.onStop()
            }

            override fun onAbandoned() { // 中断
                Log.i("controller", "onAbandoned")
            }
        }/*.controller*/
    }.controller
}

@Composable
fun retainDataController(): MyDataController {
    return retain {
        object: RetainObserver {
            val controller = MyDataController()
            private var scope: CoroutineScope? = null

            override fun onEnteredComposition() { // 进入
                Log.i("retain", "onEnteredComposition")
                controller.onStart()
            }

            override fun onExitedComposition() { // 离开
                Log.i("retain", "onExitedComposition")
                controller.onStop()
            }

            override fun onRetained() { // 最先执行 保留：启动/建立资源
                Log.i("retain", "onRetained: 创建长期资源")

                // SupervisorJob() 子协程隔离：一个子协程异常取消后，不会影响其它子协程继续执行
                // 普通的 Job, 同样情形下会中断属于该作用域的所有协程
                scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
                scope?.launch {
                    Log.i("retain", "开始预加载数据")
                }
            }

            override fun onRetired() { // 退休了: 释放正常使用过的资源
                Log.i("retain", "onRetired")
                scope?.cancel()
            }

            override fun onUnused() { // 未使用: 对象创建出来了，但最终根本没进入“被保留”状态
                Log.i("retain", "onUnused")
                scope?.cancel()
            }

        }/*.controller*/
    }.controller
    /*
     * 初始执行 onRetained  onEnteredComposition
     * 配置更改后 onExitedComposition onRetired onRetained onEnteredComposition
     * 退出界面 onExitedComposition onRetired
     */
}

@Preview(showBackground = true)
@Composable
fun VideoScreen() {
    val playerManager = rememberVideoPlayerManager()

    // UI 层只需要正常使用 playerManager 即可，不需要关心它的生命周期释放问题
    Button(modifier = Modifier.padding(top = 100.dp), onClick = {
        playerManager.play()
    }) {
        Text("Play")
    }
}

@Preview(showBackground = true)
@Composable
fun ControllerScreen() {
    val controller = rememberDataController()

    Button(modifier = Modifier.padding(top = 180.dp), onClick = {
        // ...
        controller.test()
    }) {
        Text("Controller")
    }
}

@Preview(showBackground = true)
@Composable
fun RetainControllerScreen() {
    val retainController = retainDataController()

    Button(modifier = Modifier.padding(top = 280.dp), onClick = {
        // ...
        retainController.test()
    }) {
        Text("retain Controller")
    }
}