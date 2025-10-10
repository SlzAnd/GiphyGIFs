package com.andrews.giphygifs.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.andrews.giphygifs.ui.theme.GiphyGIFsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GiphyGIFsTheme {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->

                }
            }
        }
    }
}