package com.stonestcompose.vm

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.stonestcompose.ui.navi.NaviEditRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class BaseViewModel(val savedStateHandle: SavedStateHandle): ViewModel() {

    fun test() {
        viewModelScope.launch {
            delay(1000)
            // 获取通过 data class 路由类 传递的参数
            val route = savedStateHandle.toRoute<NaviEditRoute>()
            Log.d("BaseViewModel", "test() ${route.articleId}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("BaseViewModel", "onCleared()")
    }
}