package com.kyant.aura.compose

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.annotation.FloatRange
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.kyant.aura.core.dynamiccolor.DynamicScheme
import com.kyant.aura.core.dynamiccolor.Variant
import com.kyant.aura.core.hct.Hct
import com.kyant.aura.core.scheme.DynamicSchemes

@Immutable
data class AuraSchemeOptions(
    val sourceColor: Color,
    val variant: Variant,
    val isDark: Boolean,
    @FloatRange(-1.0, 1.0)
    val contrastLevel: Float
) {
    @Stable
    fun asDynamicScheme(): DynamicScheme {
        return DynamicSchemes.createDynamicScheme(
            sourceColorHct = Hct(sourceColor.toArgb()),
            variant = variant,
            isDark = isDark,
            contrastLevel = contrastLevel.toDouble()
        )
    }

    @Stable
    fun asAuraColorScheme(): ColorScheme {
        return asDynamicScheme().asColorScheme()
    }

    companion object {
        val Default: AuraSchemeOptions
            @Composable get() = AuraSchemeOptions(
                sourceColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    colorResource(android.R.color.system_accent1_600)
                } else {
                    Color(0xFF475D92)
                },
                variant = Variant.TONAL_SPOT,
                isDark = isSystemInDarkTheme(),
                contrastLevel = getSystemContrast(LocalContext.current)
            )
    }
}

private fun DynamicScheme.asColorScheme(): ColorScheme {
    return ColorScheme(
        primary = Color(primary),
        onPrimary = Color(onPrimary),
        primaryContainer = Color(primaryContainer),
        onPrimaryContainer = Color(onPrimaryContainer),
        inversePrimary = Color(inversePrimary),
        secondary = Color(secondary),
        onSecondary = Color(onSecondary),
        secondaryContainer = Color(secondaryContainer),
        onSecondaryContainer = Color(onSecondaryContainer),
        tertiary = Color(tertiary),
        onTertiary = Color(onTertiary),
        tertiaryContainer = Color(tertiaryContainer),
        onTertiaryContainer = Color(onTertiaryContainer),
        background = Color(background),
        onBackground = Color(onBackground),
        surface = Color(surface),
        onSurface = Color(onSurface),
        surfaceVariant = Color(surfaceVariant),
        onSurfaceVariant = Color(onSurfaceVariant),
        surfaceTint = Color(surfaceTint),
        inverseSurface = Color(inverseSurface),
        inverseOnSurface = Color(inverseOnSurface),
        error = Color(error),
        onError = Color(onError),
        errorContainer = Color(errorContainer),
        onErrorContainer = Color(onErrorContainer),
        outline = Color(outline),
        outlineVariant = Color(outlineVariant),
        scrim = Color(scrim),
        surfaceBright = Color(surfaceBright),
        surfaceDim = Color(surfaceDim),
        surfaceContainer = Color(surfaceContainer),
        surfaceContainerHigh = Color(surfaceContainerHigh),
        surfaceContainerHighest = Color(surfaceContainerHighest),
        surfaceContainerLow = Color(surfaceContainerLow),
        surfaceContainerLowest = Color(surfaceContainerLowest),
    )
}

private fun getSystemContrast(context: Context): Float {
    val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager?
    return if (uiModeManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        uiModeManager.contrast
    } else {
        0f
    }
}
