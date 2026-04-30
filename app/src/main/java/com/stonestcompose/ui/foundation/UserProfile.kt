package com.stonestcompose.ui.foundation

import kotlinx.serialization.Serializable

// 定义一个复杂的数据模型，并标记为可序列化； {} 是 插件自动生成的
@Serializable
data class UserProfile(
    val id: String,
    val username: String,
    val loginCount: Int,
    val isPremium: Boolean
)


data class ArticleSimpleData(
    val id: String,
    val content: String?
)