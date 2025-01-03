package com.kyant.aura.demo.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kyant.aura.compose.AuraMaterialTheme

class ComposePaletteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AuraMaterialTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    PaletteContent()
                }
            }
        }
    }
}
