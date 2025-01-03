package com.kyant.aura.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AuraMaterialTheme(
    schemeOptions: AuraSchemeOptions = AuraSchemeOptions.Default,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = schemeOptions.asAuraColorScheme(),
        content = content
    )
}
