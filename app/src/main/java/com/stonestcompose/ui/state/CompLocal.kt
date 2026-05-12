package com.stonestcompose.ui.state

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

private data class UserInfo(val name: String, val isAdmin: Boolean)
// 关联对象若重组，使用它的地方 local#current 处会重组
//private val LocalUserInfo = compositionLocalOf { UserInfo("default", false) }

// 关联对象若重组，整个 CompositionLocalProvider中的 content ，即composable 都会重组
private val LocalUserInfo = staticCompositionLocalOf { UserInfo("default", false) }


@Composable
@Preview(showBackground = true)
fun CompLocalTest() {
//     state 记忆
    var currentUser by remember { mutableStateOf(UserInfo("Manager", true)) }

    // 通过 CompositionLocalProvider 向下传递，本质是定义了共享它的作用域
    CompositionLocalProvider(LocalUserInfo provides currentUser) {
        // 这里的子组件及其所有后代都可以访问到 currentUser
        Dashboard1()
        Dashboard2() // 内部使用 local.current
    }

    val user = LocalUserInfo.current
    Text(text = "hi, ${user.name}! Admin: ${user.isAdmin}",
        modifier = Modifier.padding(top = 40.dp))

    Button(onClick = {
        currentUser = UserInfo("xx", false)
    }, modifier = Modifier.padding(top = 100.dp)) {
        Text("change user：${currentUser}")
    }
}

@Composable
private fun Dashboard1() {
    SideEffect {
        Log.d("Dashboard1", "Dashboard1: 进入组合")
    }
    Dashboard3()
}

@Composable
private fun Dashboard3() {
    SideEffect {
        Log.d("Dashboard3", "Dashboard3: 进入组合")
    }
}


@Composable
private fun Dashboard2() {
    SideEffect {
        Log.d("Dashboard2", "Dashboard2: 进入组合")
    }
    UserInfoLabel()
}

@Composable
private fun UserInfoLabel() {
    SideEffect {
        Log.d("UserInfoLabel", "UserInfoLabel: 进入组合")
    }
    // 获取当前作用域内的值
    val user = LocalUserInfo.current
    Text(text = "Welcome, ${user.name}! Admin: ${user.isAdmin}")
}