package com.kyant.aura.demo.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
import com.kyant.aura.core.dynamiccolor.MaterialDynamicColors
import com.kyant.aura.core.dynamiccolor.Variant
import com.kyant.aura.core.hct.Hct
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSchemeContent(
    schemeOptions: AuraSchemeOptions,
    onSchemeOptionsChange: (AuraSchemeOptions) -> Unit
) {
    val scheme = remember(schemeOptions) { schemeOptions.asDynamicScheme() }
    val mdc = remember { MaterialDynamicColors() }
    val colors = remember { mdc.allDynamicColors().map { it() } }

    Column(Modifier.safeDrawingPadding()) {
        val variantSliderState = remember {
            SliderState(
                value = schemeOptions.variant.ordinal.toFloat(),
                steps = Variant.entries.size - 2,
                valueRange = 0f..(Variant.entries.size - 1).toFloat()
            )
        }
        val hueSliderState = remember {
            SliderState(
                value = Hct(schemeOptions.sourceColor.toArgb()).hue.toFloat(),
                valueRange = 0f..360f
            )
        }
        val contrastLevelSliderState = remember {
            SliderState(
                value = schemeOptions.contrastLevel,
                steps = 9,
                valueRange = -1f..1f
            )
        }

        LaunchedEffect(Unit) {
            val update = {
                onSchemeOptionsChange(
                    schemeOptions.copy(
                        variant = Variant.entries[variantSliderState.value.toInt()],
                        sourceColor = Color(
                            Hct(schemeOptions.sourceColor.toArgb()).copy(hue = hueSliderState.value.toDouble()).asArgb()
                        ),
                        contrastLevel = contrastLevelSliderState.value
                    )
                )
            }
            launch {
                merge(
                    snapshotFlow { variantSliderState.value },
                    snapshotFlow { hueSliderState.value },
                    snapshotFlow { contrastLevelSliderState.value }
                ).collectLatest { update() }
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
                        "Variant",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        schemeOptions.variant.name.replace('_', ' '),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Slider(variantSliderState)
            }
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
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Contrast Level",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "${(contrastLevelSliderState.value * 10).fastRoundToInt().toFloat() / 10}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Slider(contrastLevelSliderState)
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(colors) { dc ->
                val hct = scheme.getHct(dc)
                ColorCard(scheme, dc, hct)
            }
        }
    }
}
