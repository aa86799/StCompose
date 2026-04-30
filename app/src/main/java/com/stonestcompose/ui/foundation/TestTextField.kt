package com.stonestcompose.ui.foundation

import com.stonestcompose.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stonestcompose.util.ToastUtil

@Preview(showBackground = true)
@Composable
fun TestTextField() {
    var text by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    Column {
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xa0a0eded)),
            label = { // label 在textField 之上的提示标签
                Text("label 标签", color = Color.Red, fontSize = 40.sp)
            },
            singleLine = true
        )

        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            leadingIcon = {
                Image(painterResource(id = R.mipmap.star_cloud), null,
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp))
            },
            modifier = Modifier.padding(top = 20.dp)
        )

        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            leadingIcon = { // 前面的, @Composable 就行，不是一定要 Icon
                Text("联系人：")
            },
            trailingIcon = { // 尾部的
                Button({
                    ToastUtil.showToast("click it")
                }) {
                    Text("@163.com")
                }
            },
            singleLine = true,
            modifier = Modifier.padding(top = 20.dp),
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                color = Color(0xFF0079D3)
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Cyan, // 获得焦点内容背景
                unfocusedContainerColor = Color.LightGray, // 失去焦点内容背景
                focusedTextColor = Color.White, //获得焦点文本颜色
                unfocusedTextColor = Color.Green // 失去焦点文本颜色
                // 文本颜色会优先使用 前面 textStyle中设定的颜色；若 textStyle中未设置，才使用这里的“焦点/非焦点颜色”
            )
        )

        // pwd
        var pwdText by remember{mutableStateOf("")}
        var passwordHidden by remember{ mutableStateOf(true)}
        TextField(
            value = pwdText,
            textStyle = TextStyle(fontSize = 40.sp),
            onValueChange = {
                pwdText = it
            },
            // 尾部图标
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordHidden = !passwordHidden
                    }
                ){
                    Icon(imageVector = if (passwordHidden) Icons.Filled.RemoveRedEye else Icons.Outlined.RemoveRedEye, "" +
                            "显示/隐藏密码")
                }
            },
            label = {
                Text("密码", fontSize = 30.sp)
            },
            // 视觉效果变换
            visualTransformation = if(passwordHidden) PasswordVisualTransformation() else VisualTransformation.None
        )

        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = {
                Text("输入转大写", fontSize = 20.sp)
            },
            visualTransformation = UpperCaseTransformation()
        )

        TextField(
            value = phoneNumber,
            onValueChange = {
                // 只有当新输入的文字全是数字时，才允许赋值; 并只允许 <= 11位长度
                if (it.all { it.isDigit() }) {
                    if (it.length > 11) {
                        phoneNumber = it.substring(0, 11)
                    } else {
                        phoneNumber = it
                    }
                }
            },
            label = {
                Text("输入手机号码后，自动分段", fontSize = 20.sp)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            visualTransformation = PhoneNumberTransformation()
        )

        TestBasicTextField()
    }
}

private class UpperCaseTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // 1. 改变长相：转成大写
        val transformedText = text.text.uppercase()

        // 2. 映射光标：因为长度没变，所以是一对一映射 (Identity)
        return TransformedText(
            AnnotatedString(transformedText),
            OffsetMapping.Identity // 一对一光标映射
        )
    }
}

private class PhoneNumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originText = text.text
        var formatText = ""
        // 构造显示的字符串
        for (i in originText.indices) {
            formatText += originText[i]
            // 在第 3 位和第 7 位数字后面加 "-" 符号
            if (i == 2 || i == 6) formatText += "-"

        }
        // 定义光标映射逻辑
        val numberOffsetMapping = object : OffsetMapping {
            // 原始位置 -> 显示位置
            // 例如：原始第4个数字，因为前面加了1个空格，所以显示在第5个位置
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset + 1
                return offset + 2
            }

            // 显示位置 -> 原始位置
            // 例如：用户点在第5个位置(显示)，系统要知道这对应原始字符串的第4个数字
            override fun transformedToOriginal(offset: Int): Int {
                // 123-4567-8901  共13位
                // 12345678901  共11位
                return if (offset >= 9) offset - 2
                else if (offset >= 4) offset - 1
                else  offset
            }
        }

        // 2. 映射光标：因为长度没变，所以是一对一映射 (Identity)
        return TransformedText(
            AnnotatedString(formatText),
            numberOffsetMapping
        )
    }
}


@Composable
private fun TestBasicTextField() {
    // BasicTextField 有更高的自定义效果
    // decorationBox 中 调用 innerTextField() 完成输入框的构建
    var text by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x66ff00ff)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
            },
            modifier = Modifier
                .background(Color.White, CircleShape)
                .height(35.dp)
                .fillMaxWidth(),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(painterResource(id = R.drawable.baseline_contact_mail_24), null)
                    }
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        innerTextField()
                    }
                    IconButton(
                        onClick = { },
                    ) {
                        Icon(Icons.Filled.Send, null)
                    }
                }
            }
        )
    }
}