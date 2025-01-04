package com.kyant.aura.demo.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
import com.kyant.aura.core.dynamiccolor.DynamicColor
import com.kyant.aura.core.dynamiccolor.DynamicScheme
import com.kyant.aura.core.hct.Hct

@Composable
fun ColorCard(
    dynamicScheme: DynamicScheme,
    dynamicColor: DynamicColor,
    hct: Hct
) {
    val color = Color(dynamicScheme.getArgb(dynamicColor))

    Card(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(128.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = color,
            contentColor = if (hct.tone > 55) Color.Black else Color.White
        )
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dynamicColor.name.replace('_', ' ')
            )
            Text(
                text = "${hct.hue.fastRoundToInt()}H " +
                        "${hct.chroma.fastRoundToInt()}C " +
                        "${hct.tone.fastRoundToInt()}T",
            )
        }
    }
}
