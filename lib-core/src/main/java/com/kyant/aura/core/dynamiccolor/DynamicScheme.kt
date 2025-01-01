/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kyant.aura.core.dynamiccolor

import com.kyant.aura.core.hct.Hct
import com.kyant.aura.core.palettes.TonalPalette
import com.kyant.aura.core.utils.MathUtils

/**
 * Provides important settings for creating colors dynamically, and 6 color palettes. Requires:
 * 1. A color. (source color)
 * 2. A theme. (Variant)
 * 3. Whether its dark mode.
 * 4. Contrast level. (-1 to 1, currently contrast ratio 3.0 and 7.0)
 */
open class DynamicScheme(
    val sourceColorHct: Hct,
    val variant: Variant,
    val isDark: Boolean,
    val contrastLevel: Double,
    val primaryPalette: TonalPalette,
    val secondaryPalette: TonalPalette,
    val tertiaryPalette: TonalPalette,
    val neutralPalette: TonalPalette,
    val neutralVariantPalette: TonalPalette,
    val errorPalette: TonalPalette = TonalPalette.fromHueAndChroma(25.0, 84.0)
) {
    private val dynamicColors = MaterialDynamicColors()

    val sourceColorArgb: Int = sourceColorHct.asArgb()

    val isMonochrome: Boolean = variant.schemeType == SchemeType.MONOCHROME

    val isFidelity: Boolean = variant.schemeType == SchemeType.FIDELITY

    @Suppress("NOTHING_TO_INLINE")
    inline fun getHct(dynamicColor: DynamicColor): Hct {
        return dynamicColor.getHct(this)
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun getArgb(dynamicColor: DynamicColor): Int {
        return dynamicColor.getArgb(this)
    }

    val primaryPaletteKeyColor: Int
        get() = getArgb(dynamicColors.primaryPaletteKeyColor())

    val secondaryPaletteKeyColor: Int
        get() = getArgb(dynamicColors.secondaryPaletteKeyColor())

    val tertiaryPaletteKeyColor: Int
        get() = getArgb(dynamicColors.tertiaryPaletteKeyColor())

    val neutralPaletteKeyColor: Int
        get() = getArgb(dynamicColors.neutralPaletteKeyColor())

    val neutralVariantPaletteKeyColor: Int
        get() = getArgb(dynamicColors.neutralVariantPaletteKeyColor())

    val background: Int
        get() = getArgb(dynamicColors.background())

    val onBackground: Int
        get() = getArgb(dynamicColors.onBackground())

    val surface: Int
        get() = getArgb(dynamicColors.surface())

    val surfaceDim: Int
        get() = getArgb(dynamicColors.surfaceDim())

    val surfaceBright: Int
        get() = getArgb(dynamicColors.surfaceBright())

    val surfaceContainerLowest: Int
        get() = getArgb(dynamicColors.surfaceContainerLowest())

    val surfaceContainerLow: Int
        get() = getArgb(dynamicColors.surfaceContainerLow())

    val surfaceContainer: Int
        get() = getArgb(dynamicColors.surfaceContainer())

    val surfaceContainerHigh: Int
        get() = getArgb(dynamicColors.surfaceContainerHigh())

    val surfaceContainerHighest: Int
        get() = getArgb(dynamicColors.surfaceContainerHighest())

    val onSurface: Int
        get() = getArgb(dynamicColors.onSurface())

    val surfaceVariant: Int
        get() = getArgb(dynamicColors.surfaceVariant())

    val onSurfaceVariant: Int
        get() = getArgb(dynamicColors.onSurfaceVariant())

    val inverseSurface: Int
        get() = getArgb(dynamicColors.inverseSurface())

    val inverseOnSurface: Int
        get() = getArgb(dynamicColors.inverseOnSurface())

    val outline: Int
        get() = getArgb(dynamicColors.outline())

    val outlineVariant: Int
        get() = getArgb(dynamicColors.outlineVariant())

    val shadow: Int
        get() = getArgb(dynamicColors.shadow())

    val scrim: Int
        get() = getArgb(dynamicColors.scrim())

    val surfaceTint: Int
        get() = getArgb(dynamicColors.surfaceTint())

    val primary: Int
        get() = getArgb(dynamicColors.primary())

    val onPrimary: Int
        get() = getArgb(dynamicColors.onPrimary())

    val primaryContainer: Int
        get() = getArgb(dynamicColors.primaryContainer())

    val onPrimaryContainer: Int
        get() = getArgb(dynamicColors.onPrimaryContainer())

    val inversePrimary: Int
        get() = getArgb(dynamicColors.inversePrimary())

    val secondary: Int
        get() = getArgb(dynamicColors.secondary())

    val onSecondary: Int
        get() = getArgb(dynamicColors.onSecondary())

    val secondaryContainer: Int
        get() = getArgb(dynamicColors.secondaryContainer())

    val onSecondaryContainer: Int
        get() = getArgb(dynamicColors.onSecondaryContainer())

    val tertiary: Int
        get() = getArgb(dynamicColors.tertiary())

    val onTertiary: Int
        get() = getArgb(dynamicColors.onTertiary())

    val tertiaryContainer: Int
        get() = getArgb(dynamicColors.tertiaryContainer())

    val onTertiaryContainer: Int
        get() = getArgb(dynamicColors.onTertiaryContainer())

    val error: Int
        get() = getArgb(dynamicColors.error())

    val onError: Int
        get() = getArgb(dynamicColors.onError())

    val errorContainer: Int
        get() = getArgb(dynamicColors.errorContainer())

    val onErrorContainer: Int
        get() = getArgb(dynamicColors.onErrorContainer())

    val primaryFixed: Int
        get() = getArgb(dynamicColors.primaryFixed())

    val primaryFixedDim: Int
        get() = getArgb(dynamicColors.primaryFixedDim())

    val onPrimaryFixed: Int
        get() = getArgb(dynamicColors.onPrimaryFixed())

    val onPrimaryFixedVariant: Int
        get() = getArgb(dynamicColors.onPrimaryFixedVariant())

    val secondaryFixed: Int
        get() = getArgb(dynamicColors.secondaryFixed())

    val secondaryFixedDim: Int
        get() = getArgb(dynamicColors.secondaryFixedDim())

    val onSecondaryFixed: Int
        get() = getArgb(dynamicColors.onSecondaryFixed())

    val onSecondaryFixedVariant: Int
        get() = getArgb(dynamicColors.onSecondaryFixedVariant())

    val tertiaryFixed: Int
        get() = getArgb(dynamicColors.tertiaryFixed())

    val tertiaryFixedDim: Int
        get() = getArgb(dynamicColors.tertiaryFixedDim())

    val onTertiaryFixed: Int
        get() = getArgb(dynamicColors.onTertiaryFixed())

    val onTertiaryFixedVariant: Int
        get() = getArgb(dynamicColors.onTertiaryFixedVariant())

    val controlActivated: Int
        get() = getArgb(dynamicColors.controlActivated())

    val controlNormal: Int
        get() = getArgb(dynamicColors.controlNormal())

    val controlHighlight: Int
        get() = getArgb(dynamicColors.controlHighlight())

    val textPrimaryInverse: Int
        get() = getArgb(dynamicColors.textPrimaryInverse())

    val textSecondaryAndTertiaryInverse: Int
        get() = getArgb(dynamicColors.textSecondaryAndTertiaryInverse())

    val textPrimaryInverseDisableOnly: Int
        get() = getArgb(dynamicColors.textPrimaryInverseDisableOnly())

    val textSecondaryAndTertiaryInverseDisabled: Int
        get() = getArgb(dynamicColors.textSecondaryAndTertiaryInverseDisabled())

    val textHintInverse: Int
        get() = getArgb(dynamicColors.textHintInverse())

    companion object {
        val PrimaryPalette = { s: DynamicScheme -> s.primaryPalette }
        val SecondaryPalette = { s: DynamicScheme -> s.secondaryPalette }
        val TertiaryPalette = { s: DynamicScheme -> s.tertiaryPalette }
        val NeutralPalette = { s: DynamicScheme -> s.neutralPalette }
        val NeutralVariantPalette = { s: DynamicScheme -> s.neutralVariantPalette }
        val ErrorPalette = { s: DynamicScheme -> s.errorPalette }

        /**
         * Given a set of hues and set of hue rotations, locate which hues the source color's hue is
         * between, apply the rotation at the same index as the first hue in the range, and return the
         * rotated hue.
         *
         * @param sourceColorHct The color whose hue should be rotated.
         * @param hues           A set of hues.
         * @param rotations      A set of hue rotations.
         * @return Color's hue with a rotation applied.
         */
        @JvmStatic
        fun getRotatedHue(sourceColorHct: Hct, hues: DoubleArray, rotations: DoubleArray): Double {
            val sourceHue = sourceColorHct.hue
            if (rotations.size == 1) {
                return MathUtils.sanitizeDegreesDouble(sourceHue + rotations[0])
            }
            val size = hues.size
            for (i in 0..(size - 2)) {
                val thisHue = hues[i]
                val nextHue = hues[i + 1]
                if (thisHue < sourceHue && sourceHue < nextHue) {
                    return MathUtils.sanitizeDegreesDouble(sourceHue + rotations[i])
                }
            }
            // If this statement executes, something is wrong, there should have been a rotation
            // found using the arrays.
            return sourceHue
        }
    }
}
