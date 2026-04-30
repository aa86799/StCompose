package com.stonestcompose.ui.state

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stonestcompose.ui.foundation.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.serializer

@Preview(showBackground = true)
@Composable
fun TestState() {
    /*
     * AS 有时会报 Type 'MutableState<Int>' has no method 'setValue ...
     * 是因为 AS import 无法自动识别 by 关键字（属性委托）所需的扩展函数
     * 手动添加导入:
     *      import androidx.compose.runtime.getValue
     *      import androidx.compose.runtime.setValue
     *
     * 不使用 by 委托语法，也可以直接使用 = 号来接收 MutableState 对象，
     * 但是在后续使用时每次都需要加上 .value。这种写法不需要导入那两个扩展函数
     */
     var currentStep by remember { mutableStateOf(1) }
     Column {
         Text("当前进度：第 $currentStep 步", modifier = Modifier.padding(top = 100.dp))

         Button(onClick = { currentStep++ }) {
             Text("下一步")
         }
     }

    /**/
    val x1 by rememberSaveable{ mutableStateOf("") }
    val flow: Flow<Int>? = null
//        flow?.collectAsStateWithLifecycle()
//        flow?.stateIn()

    var colorV = Color.Red
    val animState by animateColorAsState(colorV)
    colorV = Color.Blue


//    val snackbarHostState  by remember { SnackbarHostState }
//    val datePickerState by remember { mutableStateOf(DatePickerState()) }

    //
//    val ss : SavedStateHandle
//    ss.saveable(saver = mapSaver()) {}
//    TextFieldValue.Saver


    /* test rememberSerializable */
    val saveData = rememberSerializable(serializer = serializer<UserProfile>()) {
        UserProfile(
            id = "U001",
            username = "Stone1",
            loginCount = 1,
            isPremium = false
        )
    }
    Log.i("TestText: ", saveData.id)

    val state = rememberSerializable(init = {
        mutableStateOf(UserProfile(
            id = "U002",
            username = "Stone2",
            loginCount = 1,
            isPremium = false
        ))
    })
}