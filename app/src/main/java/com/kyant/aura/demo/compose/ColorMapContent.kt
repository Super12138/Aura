package com.kyant.aura.demo.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.aura.core.hct.Hct

@Composable
fun ColorMapContent() {
    val chroma = 200.0

    Column(
        Modifier
            .safeDrawingPadding()
            .padding(24.dp)
    ) {
        for (tone in 100 downTo 0) {
            Row {
                for (hue in 0..360) {
                    Box(
                        Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(Color(Hct(hue.toDouble(), chroma, tone.toDouble()).asArgb()))
                    )
                }
            }
        }
    }
}
