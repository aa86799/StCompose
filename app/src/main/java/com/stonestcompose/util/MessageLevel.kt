
package com.stonestcompose.util

import android.widget.Toast
import com.stonestcompose.MyApplication
import es.dmoral.toasty.Toasty

enum class MessageLevel {
    NORMAL, INFO, WARNING, ERROR, SUCCESS
    /*
     * 对应 toast的背景不同： 黑、蓝、黄、红、绿
     */
}

class ToastUtil {

    companion object {
        @JvmStatic
        fun showToast(msg: String, messageLevel: MessageLevel = MessageLevel.NORMAL, duration: Int = Toast.LENGTH_SHORT) {
            val context = MyApplication.getInstance().topActivity ?: return
            when (messageLevel) {
                MessageLevel.NORMAL -> Toasty.normal(context, msg, duration).show() // 黑背景
                MessageLevel.INFO -> Toasty.info(context, msg, duration).show() // 蓝背景
                MessageLevel.WARNING -> Toasty.warning(context, msg, duration).show() // 黄背景
                MessageLevel.ERROR -> Toasty.error(context, msg, duration).show() // 红背景
                MessageLevel.SUCCESS -> Toasty.success(context, msg, duration).show() // 绿背景
            }
        }

        fun showErrToast(msg: String) {
            return showToast(msg, MessageLevel.ERROR, Toast.LENGTH_LONG)
        }
    }
}