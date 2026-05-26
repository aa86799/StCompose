package com.stonestcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.stonestcompose.ui.navi.CustomNavigation
import com.stonestcompose.ui.navi.StandardNavigation
import com.stonestcompose.ui.theme.StComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StComposeTheme {
//                CustomNavigation()

                StandardNavigation()
            }
        }
    }
}
