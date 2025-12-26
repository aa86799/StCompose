package com.stonestcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

abstract class BaseMviViewModel : ViewModel() {

    /**
     * 事件意图, 点击事件、刷新等都是Intent。表示用户的主动操作
     */
    private val userIntent = Channel<BaseIntent>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            userIntent.consumeAsFlow().distinctUntilChanged().collect {
                handleUserIntent(it)
            }
        }
    }

    abstract fun handleUserIntent(intent: BaseIntent)

    /**
     * 分发意图
     *
     * 仅此一个 公开函数。供 V 调用
     */
    open fun sendIntent(intent: BaseIntent) {
        viewModelScope.launch {
            userIntent.send(intent)
        }
    }

}