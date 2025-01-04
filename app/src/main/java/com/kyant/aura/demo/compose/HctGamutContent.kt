package com.kyant.aura.demo.compose

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
import com.kyant.aura.core.hct.Hct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HctGamutContent() {
    val chromaSliderState = remember {
        SliderState(
            value = 200f,
            valueRange = 0f..200f
        )
    }
    val tones = 0..100
    val hues = 0 until 360
    var imageBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        val bitmap = Bitmap.createBitmap(360, 101, Bitmap.Config.ARGB_8888)
        snapshotFlow { chromaSliderState.value }.collectLatest {
            withContext(Dispatchers.Default) {
                val chroma = it.toDouble()
                for (tone in tones) {
                    for (hue in hues) {
                        bitmap.setPixel(hue, 100 - tone, Hct.toArgb(hue.toDouble(), chroma, tone.toDouble()))
                    }
                }
                imageBitmap = bitmap.asImageBitmap()
            }
        }
    }

    Column(
        Modifier
            .safeDrawingPadding()
            .padding(24.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Chroma",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "${chromaSliderState.value.fastRoundToInt()}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Slider(chromaSliderState)

        Canvas(Modifier.fillMaxSize()) {
            imageBitmap?.let {
                drawImage(
                    it,
                    dstSize = IntSize(
                        size.width.toInt(),
                        it.height * size.width.toInt() / it.width
                    )
                )
            }
        }
    }
}
