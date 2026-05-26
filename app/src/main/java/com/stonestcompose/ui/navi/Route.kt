package com.stonestcompose.ui.navi

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

/** 带有关闭按钮的容器页面 */
@Serializable
object CloseablePageContainer

@Serializable
object StateCaseRoute

@Serializable
data class NaviArgsRoute(val articleId: Int)

@Serializable
data class NaviEditRoute(val articleId: Int)

@Serializable
object TestTextRoute

@Serializable
object TestButtonRoute

@Serializable
object TestTextFieldRoute

@Serializable
object TestIconAndIconButtonRoute