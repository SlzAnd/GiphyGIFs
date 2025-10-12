package com.andrews.giphygifs.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.andrews.giphygifs.ui.navigation.NavGraph
import com.andrews.giphygifs.ui.theme.GiphyGIFsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            GiphyGIFsTheme {
                NavGraph(navController = navController)
            }
        }
    }
}