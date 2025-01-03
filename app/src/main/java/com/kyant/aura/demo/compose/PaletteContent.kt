package com.kyant.aura.demo.compose

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
import com.kyant.aura.compose.AuraSchemeOptions
import com.kyant.aura.core.hct.Hct
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaletteContent() {
    Column(Modifier.safeDrawingPadding()) {
        val schemeOptions = AuraSchemeOptions.Default
        val hueSliderState = remember {
            SliderState(
                value = Hct(schemeOptions.sourceColor.toArgb()).hue.toFloat(),
                valueRange = 0f..360f
            )
        }
        var colorProvider by remember {
            mutableStateOf(
                DynamicColorProvider(
                    schemeOptions.copy(
                        sourceColor = Color(
                            Hct(schemeOptions.sourceColor.toArgb())
                                .copy(hue = hueSliderState.value.toDouble())
                                .asArgb()
                        )
                    ).asDynamicScheme()
                )
            )
        }

        LaunchedEffect(Unit) {
            launch {
                snapshotFlow { hueSliderState.value }
                    .collectLatest { hue ->
                        colorProvider = DynamicColorProvider(
                            schemeOptions.copy(
                                sourceColor = Color(
                                    Hct(schemeOptions.sourceColor.toArgb())
                                        .copy(hue = hue.toDouble())
                                        .asArgb()
                                )
                            ).asDynamicScheme()
                        )
                    }
            }
        }

        Column(
            Modifier.padding(horizontal = 24.dp)
        ) {
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Hue",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "${hueSliderState.value.fastRoundToInt()}°",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Slider(hueSliderState)
            }

            Row(
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 24.dp)
            ) {
                colorProvider.colors.forEachIndexed { i, colorList ->
                    Column(Modifier.weight(1f)) {
                        colorList.forEachIndexed shadeLoop@{ j, color ->
                            ColorCell(
                                hct = Hct(color),
                                text = "${colorProvider.colorNames[i]}-${colorProvider.shadeNumbers[j]}",
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
