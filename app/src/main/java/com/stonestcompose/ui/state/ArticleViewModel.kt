package com.stonestcompose.ui.state

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import com.stonestcompose.ui.foundation.ArticleSimpleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ArticleViewModel(val savedStateHandle: SavedStateHandle): ViewModel() {

    private val articleId = savedStateHandle.getStateFlow("ARTICLE_ID", "")

    private val _articleContent = MutableStateFlow<ArticleSimpleData?>(null)
    val articleContent = _articleContent.asStateFlow()

    fun updateArticleId(id: String) {
        savedStateHandle["ARTICLE_ID"] = id
    }

    fun updateArticle(data: ArticleSimpleData) {
        _articleContent.value = data
    }
}