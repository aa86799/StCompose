package com.stonestcompose.ui.state

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.stonestcompose.ui.foundation.ArticleSimpleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ArticleListViewModel(val savedStateHandle: SavedStateHandle): ViewModel() {

    var message by savedStateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    fun update(msg: TextFieldValue) {
        message = msg
    }

    private val _articleList = MutableStateFlow<List<ArticleSimpleData>>(emptyList())
    val articleList = _articleList.asStateFlow()

    fun getArticleList() {
        viewModelScope.launch {
            // 模拟从网络加载列表数据
            val list: MutableList<ArticleSimpleData> = mutableListOf() // 从vm获取 stateFlow 文章列表
            list.add(ArticleSimpleData("001", "content001"))
            list.add(ArticleSimpleData("002", "content002"))
            _articleList.value = list
        }
    }
}