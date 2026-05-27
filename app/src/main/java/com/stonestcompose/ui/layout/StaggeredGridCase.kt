package com.stonestcompose.ui.layout

import com.stonestcompose.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun StaggeredGridCase() {
    // 延迟加载 纵向 交错网格
    LazyVerticalStaggeredGrid(
//        columns = StaggeredGridCells.Adaptive(200.dp), // 最小列宽 100dp；会计算出一个接近的等宽值
        columns = StaggeredGridCells.Fixed(3), // 固定列数，宽度平均
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(20) {
                Image(
                    painter = painterResource(id = if (it % 2 == 0) R.mipmap.capsule_man else R.mipmap.star_cloud),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}