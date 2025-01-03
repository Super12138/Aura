package com.kyant.aura.demo.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kyant.aura.compose.AuraMaterialTheme
import com.kyant.aura.compose.AuraSchemeOptions

class ComposeColorSchemeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val defaultSchemeOptions = AuraSchemeOptions.Default
            var schemeOptions by remember { mutableStateOf(defaultSchemeOptions) }

            AuraMaterialTheme(
                schemeOptions = schemeOptions
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    ColorSchemeContent(
                        schemeOptions = schemeOptions,
                        onSchemeOptionsChange = { schemeOptions = it }
                    )
                }
            }
        }
    }
}
