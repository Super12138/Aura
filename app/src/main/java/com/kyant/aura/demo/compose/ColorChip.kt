package com.kyant.aura.demo.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kyant.aura.core.hct.Hct

@Composable
fun ColorChip(
    hct: Hct,
    text: String,
    modifier: Modifier = Modifier
) {
    val color = Color(hct.asArgb())

    Card(
        modifier,
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
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
