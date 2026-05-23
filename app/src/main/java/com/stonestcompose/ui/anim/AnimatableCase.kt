package com.stonestcompose.ui.anim

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/*
  Animatable 初始化必须是一个 float值参数；而 animateTo()中的 targetValue却是 泛型T
  这种设计体现了 Compose 动画系统的核心哲学：万物皆可向量化
  Animatable 可以支持任何数据类型，只要提供一个“转换器”
   */

@Composable
@Preview(showBackground = true)
fun AnimatableCase1() { // offset 数值变化 动画
    var moved by remember { mutableStateOf(false) }

    // 定义 Animatable
    val offsetX = remember { androidx.compose.animation.core.Animatable(0f) }

    // 触发动画
    LaunchedEffect(moved) {
        // 当前值平滑地变动到目标值
        offsetX.animateTo(
            targetValue = if (moved) 50f else 0f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy)
        )

//        offsetX.snapTo()
//        offsetX.stop()
//        offsetX.animateDecay()
        /*
   snapTo(targetValue): 瞬间将值设置为目标值，不产生动画过程。常用于手势跟随后的位置重置。
   stop():立即停止正在进行的动画。
   animateDecay(initialVelocity, animationSpec):衰减动画。常用于处理“惯性滚动”，即根据一个初始速度让值自然减速停止。
         */
    }

    Surface(
        Modifier.fillMaxSize(),
        color = Color.Green
    ) {
        Box(contentAlignment = Alignment.Center) { // 这一层会消化 Surface 的强约束
            Box(
                Modifier
                    .size(100.dp)
                    .offset(x = offsetX.value.dp) // 使用值
                    .background(Color.Blue)
                    .clickable { moved = !moved }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AnimatableCase2() { // 颜色变化 动画
    var isError by remember { mutableStateOf(false) }
    // 初始化时指定 Color，系统会自动匹配 Color.VectorConverter
    val colorAnim = remember { Animatable(Color.Gray) }

    LaunchedEffect(isError) {
        // targetValue 此时是 Color 类型，不是 Float
        colorAnim.animateTo(
            targetValue = if (isError) Color.Red else Color.Green,
            animationSpec = tween(3000)
        )
    }

    Box(Modifier.background(colorAnim.value).size(100.dp)
        .clickable {
            isError = !isError
        })
}

@Composable
@Preview(showBackground = true)
fun AnimatableCase3() { // offset 动画
    val targetOffset = Offset(220f, 1230f)
    //
    val offsetAnim = remember {
        androidx.compose.animation.core.Animatable(
            Offset(0f, 0f),
            Offset.VectorConverter
        )
    }

    LaunchedEffect(targetOffset) {
        // targetValue 是 Offset 类型
        offsetAnim.animateTo(targetOffset,
            animationSpec = tween(3000))
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(Color.Blue, radius = 50f, center = offsetAnim.value)
    }
}

data class MySize(val width: Float, val height: Float)
@Composable
@Preview(showBackground = true)
fun AnimatableCase4() { // 自定义类型
    // 定义转换器：如何把 MySize 转成 Vector2D，以及如何转回来
    val mySizeConverter = TwoWayConverter<MySize, AnimationVector2D>(
        convertToVector = { AnimationVector2D(it.width, it.height) },
        convertFromVector = { MySize(it.v1, it.v2) }
    )

    val anim = remember { androidx.compose.animation.core.Animatable(
        MySize(10f, 10f), mySizeConverter) }

    // 对整个对象做动画
    LaunchedEffect(Unit) {
        anim.animateTo(MySize(800f, 800f),
            tween(2500, easing = CubicBezierEasing(
                0.42f, 0f, 0.58f, 1.0f
            )))
        /*
        easing（缓动）它决定了动画随时间变化的速率。
        三次方贝塞尔曲线，定义动画节奏。
        在动画中，这种曲线描述了“时间”与“动画完成进度”之间的关系。这四个参数实际上是两个控制点的坐标。
        (x1,y1, x2,y2)
        x 坐标的限制: 因为它们代表时间轴，时间不能倒流，也不能超过总时长，所以必须在 [0, 1] 之间
        y 坐标的灵活性：y代表的是动画进度的拉伸方向，它们可以小于 0 或 大于 1
            y>1时（过冲效果）：动画会在达到终点前，“冲过”目标值，然后再折返回来。这通常用于实现 Overshoot（回弹） 效果
            y<0 时（预跳效果）：动画在开始时会先向相反方向移动，然后再向终点冲刺。这在动画设计中被称为 Anticipate（预备动作）。
        大多数标准缓动（如 EaseIn 或 EaseOut）的 y 值都设为 0 或 1，
        是因为这样可以确保动画的进度轨迹严格限制在起点（0）和终点（1）的范围之内，不会出现回弹或超出边界的情况。
         */
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(Color.Blue, Offset(100f,200f), Size(anim.value.width, anim.value.height),
            1.0f)
    }
}