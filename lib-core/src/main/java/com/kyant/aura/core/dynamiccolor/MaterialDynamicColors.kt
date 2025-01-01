/*
 * Copyright 2023 Google LLC
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

import com.kyant.aura.core.dislike.DislikeAnalyzer.fixIfDisliked
import com.kyant.aura.core.hct.HctSolver
import kotlin.math.abs

/**
 * Named colors, otherwise known as tokens, or roles, in the Material Design system.
 */
class MaterialDynamicColors {
    @Suppress("PropertyName")
    val HighestSurface = { s: DynamicScheme -> if (s.isDark) surfaceBright() else surfaceDim() }

    // Compatibility Keys Colors for Android
    fun primaryPaletteKeyColor(): DynamicColor {
        return DynamicColor(
            "primary_palette_key_color",
            DynamicScheme.PrimaryPalette,
            { s -> s.primaryPalette.keyColor.tone })
    }

    fun secondaryPaletteKeyColor(): DynamicColor {
        return DynamicColor(
            "secondary_palette_key_color",
            DynamicScheme.SecondaryPalette,
            { s -> s.secondaryPalette.keyColor.tone })
    }

    fun tertiaryPaletteKeyColor(): DynamicColor {
        return DynamicColor(
            "tertiary_palette_key_color",
            DynamicScheme.TertiaryPalette,
            { s -> s.tertiaryPalette.keyColor.tone })
    }

    fun neutralPaletteKeyColor(): DynamicColor {
        return DynamicColor(
            "neutral_palette_key_color",
            DynamicScheme.NeutralPalette,
            { s -> s.neutralPalette.keyColor.tone })
    }

    fun neutralVariantPaletteKeyColor(): DynamicColor {
        return DynamicColor(
            "neutral_variant_palette_key_color",
            DynamicScheme.NeutralVariantPalette,
            { s -> s.neutralVariantPalette.keyColor.tone })
    }

    fun background(): DynamicColor {
        return DynamicColor(
            "background",
            DynamicScheme.NeutralPalette,
            { s -> if (s.isDark) 6.0 else 98.0 },
            isBackground = true
        )
    }

    fun onBackground(): DynamicColor {
        return DynamicColor(
            "on_background",
            DynamicScheme.NeutralPalette,
            { s -> if (s.isDark) 90.0 else 10.0 },
            background = { s -> background() },
            contrastCurve = ContrastCurve(3.0, 3.0, 4.5, 7.0)
        )
    }

    fun surface(): DynamicColor {
        return DynamicColor(
            "surface",
            DynamicScheme.NeutralPalette,
            { s -> if (s.isDark) 6.0 else 98.0 },
            isBackground = true
        )
    }

    fun surfaceDim(): DynamicColor {
        return DynamicColor(
            "surface_dim",
            DynamicScheme.NeutralPalette,
            { s ->
                if (s.isDark) 6.0
                else ContrastCurve.get(87.0, 87.0, 80.0, 75.0, s.contrastLevel)
            },
            isBackground = true
        )
    }

    fun surfaceBright(): DynamicColor {
        return DynamicColor(
            "surface_bright",
            DynamicScheme.NeutralPalette,
            { s ->
                if (s.isDark) ContrastCurve.get(24.0, 24.0, 29.0, 34.0, s.contrastLevel)
                else 98.0
            },
            isBackground = true
        )
    }

    fun surfaceContainerLowest(): DynamicColor {
        return DynamicColor(
            "surface_container_lowest",
            DynamicScheme.NeutralPalette,
            { s ->
                if (s.isDark) ContrastCurve.get(4.0, 4.0, 2.0, 0.0, s.contrastLevel)
                else 100.0
            },
            isBackground = true
        )
    }

    fun surfaceContainerLow(): DynamicColor {
        return DynamicColor(
            "surface_container_low",
            DynamicScheme.NeutralPalette,
            { s ->
                if (s.isDark) {
                    ContrastCurve.get(10.0, 10.0, 11.0, 12.0, s.contrastLevel)
                } else {
                    ContrastCurve.get(96.0, 96.0, 96.0, 95.0, s.contrastLevel)
                }
            },
            isBackground = true
        )
    }

    fun surfaceContainer(): DynamicColor {
        return DynamicColor(
            "surface_container",
            DynamicScheme.NeutralPalette,
            { s ->
                if (s.isDark) {
                    ContrastCurve.get(12.0, 12.0, 16.0, 20.0, s.contrastLevel)
                } else {
                    ContrastCurve.get(94.0, 94.0, 92.0, 90.0, s.contrastLevel)
                }
            },
            isBackground = true
        )
    }

    fun surfaceContainerHigh(): DynamicColor {
        return DynamicColor(
            "surface_container_high",
            DynamicScheme.NeutralPalette,
            { s ->
                if (s.isDark) {
                    ContrastCurve.get(17.0, 17.0, 21.0, 25.0, s.contrastLevel)
                } else {
                    ContrastCurve.get(92.0, 92.0, 88.0, 85.0, s.contrastLevel)
                }
            },
            isBackground = true
        )
    }

    fun surfaceContainerHighest(): DynamicColor {
        return DynamicColor(
            "surface_container_highest",
            DynamicScheme.NeutralPalette,
            { s ->
                if (s.isDark) {
                    ContrastCurve.get(22.0, 22.0, 26.0, 30.0, s.contrastLevel)
                } else {
                    ContrastCurve.get(90.0, 90.0, 84.0, 80.0, s.contrastLevel)
                }
            },
            isBackground = true
        )
    }

    fun onSurface(): DynamicColor {
        return DynamicColor(
            "on_surface",
            DynamicScheme.NeutralPalette,
            { s -> if (s.isDark) 90.0 else 10.0 },
            background = HighestSurface,
            contrastCurve = ContrastCurve.OnAccent
        )
    }

    fun surfaceVariant(): DynamicColor {
        return DynamicColor(
            "surface_variant",
            DynamicScheme.NeutralVariantPalette,
            { s -> if (s.isDark) 30.0 else 90.0 },
            isBackground = true
        )
    }

    fun onSurfaceVariant(): DynamicColor {
        return DynamicColor(
            "on_surface_variant",
            DynamicScheme.NeutralVariantPalette,
            { s -> if (s.isDark) 80.0 else 30.0 },
            background = HighestSurface,
            contrastCurve = ContrastCurve.OnContainer
        )
    }

    fun inverseSurface(): DynamicColor {
        return DynamicColor(
            "inverse_surface",
            DynamicScheme.NeutralPalette,
            { s -> if (s.isDark) 90.0 else 20.0 }
        )
    }

    fun inverseOnSurface(): DynamicColor {
        return DynamicColor(
            "inverse_on_surface",
            DynamicScheme.NeutralPalette,
            { s -> if (s.isDark) 20.0 else 95.0 },
            background = { s -> inverseSurface() },
            contrastCurve = ContrastCurve.OnAccent
        )
    }

    fun outline(): DynamicColor {
        return DynamicColor(
            "outline",
            DynamicScheme.NeutralVariantPalette,
            { s -> if (s.isDark) 60.0 else 50.0 },
            background = HighestSurface,
            contrastCurve = ContrastCurve(1.5, 3.0, 4.5, 7.0)
        )
    }

    fun outlineVariant(): DynamicColor {
        return DynamicColor(
            "outline_variant",
            DynamicScheme.NeutralVariantPalette,
            { s -> if (s.isDark) 30.0 else 80.0 },
            background = HighestSurface,
            contrastCurve = ContrastCurve.Container
        )
    }

    fun shadow(): DynamicColor {
        return DynamicColor(
            "shadow",
            DynamicScheme.NeutralPalette,
            { s -> 0.0 }
        )
    }

    fun scrim(): DynamicColor {
        return DynamicColor(
            "scrim",
            DynamicScheme.NeutralPalette,
            { s -> 0.0 }
        )
    }

    fun surfaceTint(): DynamicColor {
        return DynamicColor(
            "surface_tint",
            DynamicScheme.PrimaryPalette,
            { s -> if (s.isDark) 80.0 else 40.0 },
            isBackground = true
        )
    }

    fun primary(): DynamicColor {
        return DynamicColor(
            "primary",
            DynamicScheme.PrimaryPalette,
            { s ->
                if (s.isMonochrome) {
                    if (s.isDark) 100.0 else 0.0
                } else {
                    if (s.isDark) 80.0 else 40.0
                }
            },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Accent,
            toneDeltaPair = { s -> ToneDeltaPair(primaryContainer(), primary(), 10.0, TonePolarity.NEARER, false) })
    }

    fun onPrimary(): DynamicColor {
        return DynamicColor(
            "on_primary",
            DynamicScheme.PrimaryPalette,
            { s ->
                if (s.isMonochrome) {
                    if (s.isDark) 10.0 else 90.0
                } else {
                    if (s.isDark) 20.0 else 100.0
                }
            },
            background = { s -> primary() },
            contrastCurve = ContrastCurve.OnAccent
        )
    }

    fun primaryContainer(): DynamicColor {
        return DynamicColor(
            "primary_container",
            DynamicScheme.PrimaryPalette,
            { s ->
                if (s.isFidelity) {
                    s.sourceColorHct.tone
                } else if (s.isMonochrome) {
                    if (s.isDark) 85.0 else 25.0
                } else {
                    if (s.isDark) 30.0 else 90.0
                }
            },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Container,
            toneDeltaPair = { s -> ToneDeltaPair(primaryContainer(), primary(), 10.0, TonePolarity.NEARER, false) })
    }

    fun onPrimaryContainer(): DynamicColor {
        return DynamicColor(
            "on_primary_container",
            DynamicScheme.PrimaryPalette,
            { s ->
                if (s.isFidelity) {
                    DynamicColor.foregroundTone(primaryContainer().tone(s), 4.5)
                } else if (s.isMonochrome) {
                    if (s.isDark) 0.0 else 100.0
                } else {
                    if (s.isDark) 90.0 else 30.0
                }
            },
            background = { s -> primaryContainer() },
            contrastCurve = ContrastCurve.OnContainer
        )
    }

    fun inversePrimary(): DynamicColor {
        return DynamicColor(
            "inverse_primary",
            DynamicScheme.PrimaryPalette,
            { s -> if (s.isDark) 40.0 else 80.0 },
            background = { s -> inverseSurface() },
            contrastCurve = ContrastCurve.Accent
        )
    }

    fun secondary(): DynamicColor {
        return DynamicColor(
            "secondary",
            DynamicScheme.SecondaryPalette,
            { s -> if (s.isDark) 80.0 else 40.0 },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Accent,
            toneDeltaPair = { s -> ToneDeltaPair(secondaryContainer(), secondary(), 10.0, TonePolarity.NEARER, false) })
    }

    fun onSecondary(): DynamicColor {
        return DynamicColor(
            "on_secondary",
            DynamicScheme.SecondaryPalette,
            { s ->
                if (s.isMonochrome) {
                    if (s.isDark) 10.0 else 100.0
                } else {
                    if (s.isDark) 20.0 else 100.0
                }
            },
            background = { s -> secondary() },
            contrastCurve = ContrastCurve.OnAccent
        )
    }

    fun secondaryContainer(): DynamicColor {
        return DynamicColor(
            "secondary_container",
            DynamicScheme.SecondaryPalette,
            { s ->
                val initialTone = if (s.isDark) 30.0 else 90.0
                if (s.isMonochrome) {
                    if (s.isDark) 30.0 else 85.0
                } else if (!s.isFidelity) {
                    initialTone
                } else {
                    findDesiredChromaByTone(s.secondaryPalette.hue, s.secondaryPalette.chroma, initialTone, !s.isDark)
                }
            },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Container,
            toneDeltaPair = { s -> ToneDeltaPair(secondaryContainer(), secondary(), 10.0, TonePolarity.NEARER, false) })
    }

    fun onSecondaryContainer(): DynamicColor {
        return DynamicColor(
            "on_secondary_container",
            DynamicScheme.SecondaryPalette,
            { s ->
                if (s.isMonochrome) {
                    if (s.isDark) 90.0 else 10.0
                } else if (!s.isFidelity) {
                    if (s.isDark) 90.0 else 30.0
                } else {
                    DynamicColor.foregroundTone(secondaryContainer().tone(s), 4.5)
                }
            },
            background = { s -> secondaryContainer() },
            contrastCurve = ContrastCurve.OnContainer
        )
    }

    fun tertiary(): DynamicColor {
        return DynamicColor(
            "tertiary",
            DynamicScheme.TertiaryPalette,
            { s ->
                if (s.isMonochrome) {
                    if (s.isDark) 90.0 else 25.0
                } else {
                    if (s.isDark) 80.0 else 40.0
                }
            },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Accent,
            toneDeltaPair = { s -> ToneDeltaPair(tertiaryContainer(), tertiary(), 10.0, TonePolarity.NEARER, false) })
    }

    fun onTertiary(): DynamicColor {
        return DynamicColor(
            "on_tertiary",
            DynamicScheme.TertiaryPalette,
            { s ->
                if (s.isMonochrome) {
                    if (s.isDark) 10.0 else 90.0
                } else {
                    if (s.isDark) 20.0 else 100.0
                }
            },
            background = { s -> tertiary() },
            contrastCurve = ContrastCurve.OnAccent
        )
    }

    fun tertiaryContainer(): DynamicColor {
        return DynamicColor(
            "tertiary_container",
            DynamicScheme.TertiaryPalette,
            { s ->
                if (s.isMonochrome) {
                    if (s.isDark) 60.0 else 49.0
                } else if (!s.isFidelity) {
                    if (s.isDark) 30.0 else 90.0
                } else {
                    val proposedHct = s.tertiaryPalette.getHct(s.sourceColorHct.tone)
                    proposedHct.fixIfDisliked().tone
                }
            },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Container,
            toneDeltaPair = { s -> ToneDeltaPair(tertiaryContainer(), tertiary(), 10.0, TonePolarity.NEARER, false) })
    }

    fun onTertiaryContainer(): DynamicColor {
        return DynamicColor(
            "on_tertiary_container",
            DynamicScheme.TertiaryPalette,
            { s ->
                if (s.isMonochrome) {
                    if (s.isDark) 0.0 else 100.0
                } else if (!s.isFidelity) {
                    if (s.isDark) 90.0 else 30.0
                } else {
                    DynamicColor.foregroundTone(tertiaryContainer().tone(s), 4.5)
                }
            },
            background = { s -> tertiaryContainer() },
            contrastCurve = ContrastCurve.OnContainer
        )
    }

    fun error(): DynamicColor {
        return DynamicColor(
            "error",
            DynamicScheme.ErrorPalette,
            { s -> if (s.isDark) 80.0 else 40.0 },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Accent,
            toneDeltaPair = { s -> ToneDeltaPair(errorContainer(), error(), 10.0, TonePolarity.NEARER, false) })
    }

    fun onError(): DynamicColor {
        return DynamicColor(
            "on_error",
            DynamicScheme.ErrorPalette,
            { s -> if (s.isDark) 20.0 else 100.0 },
            background = { s -> error() },
            contrastCurve = ContrastCurve.OnAccent
        )
    }

    fun errorContainer(): DynamicColor {
        return DynamicColor(
            "error_container",
            DynamicScheme.ErrorPalette,
            { s -> if (s.isDark) 30.0 else 90.0 },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Container,
            toneDeltaPair = { s -> ToneDeltaPair(errorContainer(), error(), 10.0, TonePolarity.NEARER, false) })
    }

    fun onErrorContainer(): DynamicColor {
        return DynamicColor(
            "on_error_container",
            DynamicScheme.ErrorPalette,
            { s ->
                if (s.isMonochrome) {
                    if (s.isDark) 90.0 else 10.0
                } else {
                    if (s.isDark) 90.0 else 30.0
                }
            },
            background = { s -> errorContainer() },
            contrastCurve = ContrastCurve.OnContainer
        )
    }

    fun primaryFixed(): DynamicColor {
        return DynamicColor(
            "primary_fixed",
            DynamicScheme.PrimaryPalette,
            { s -> if (s.isMonochrome) 40.0 else 90.0 },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Container,
            toneDeltaPair = { s -> ToneDeltaPair(primaryFixed(), primaryFixedDim(), 10.0, TonePolarity.LIGHTER, true) })
    }

    fun primaryFixedDim(): DynamicColor {
        return DynamicColor(
            "primary_fixed_dim",
            DynamicScheme.PrimaryPalette,
            { s -> if (s.isMonochrome) 30.0 else 80.0 },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Container,
            toneDeltaPair = { s -> ToneDeltaPair(primaryFixed(), primaryFixedDim(), 10.0, TonePolarity.LIGHTER, true) })
    }

    fun onPrimaryFixed(): DynamicColor {
        return DynamicColor(
            "on_primary_fixed",
            DynamicScheme.PrimaryPalette,
            { s -> if (s.isMonochrome) 100.0 else 10.0 },
            background = { s -> primaryFixedDim() },
            secondBackground = { s -> primaryFixed() },
            contrastCurve = ContrastCurve.OnAccent
        )
    }

    fun onPrimaryFixedVariant(): DynamicColor {
        return DynamicColor(
            "on_primary_fixed_variant",
            DynamicScheme.PrimaryPalette,
            { s -> if (s.isMonochrome) 90.0 else 30.0 },
            background = { s -> primaryFixedDim() },
            secondBackground = { s -> primaryFixed() },
            contrastCurve = ContrastCurve.OnContainer
        )
    }

    fun secondaryFixed(): DynamicColor {
        return DynamicColor(
            "secondary_fixed",
            DynamicScheme.SecondaryPalette,
            { s -> if (s.isMonochrome) 80.0 else 90.0 },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Container,
            toneDeltaPair = { s ->
                ToneDeltaPair(secondaryFixed(), secondaryFixedDim(), 10.0, TonePolarity.LIGHTER, true)
            })
    }

    fun secondaryFixedDim(): DynamicColor {
        return DynamicColor(
            "secondary_fixed_dim",
            DynamicScheme.SecondaryPalette,
            { s -> if (s.isMonochrome) 70.0 else 80.0 },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Container,
            toneDeltaPair = { s ->
                ToneDeltaPair(secondaryFixed(), secondaryFixedDim(), 10.0, TonePolarity.LIGHTER, true)
            })
    }

    fun onSecondaryFixed(): DynamicColor {
        return DynamicColor(
            "on_secondary_fixed",
            DynamicScheme.SecondaryPalette,
            { s -> 10.0 },
            background = { s -> secondaryFixedDim() },
            secondBackground = { s -> secondaryFixed() },
            contrastCurve = ContrastCurve.OnAccent
        )
    }

    fun onSecondaryFixedVariant(): DynamicColor {
        return DynamicColor(
            "on_secondary_fixed_variant",
            DynamicScheme.SecondaryPalette,
            { s -> if (s.isMonochrome) 25.0 else 30.0 },
            background = { s -> secondaryFixedDim() },
            secondBackground = { s -> secondaryFixed() },
            contrastCurve = ContrastCurve.OnContainer
        )
    }

    fun tertiaryFixed(): DynamicColor {
        return DynamicColor(
            "tertiary_fixed",
            DynamicScheme.TertiaryPalette,
            { s -> if (s.isMonochrome) 40.0 else 90.0 },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Container,
            toneDeltaPair = { s ->
                ToneDeltaPair(tertiaryFixed(), tertiaryFixedDim(), 10.0, TonePolarity.LIGHTER, true)
            })
    }

    fun tertiaryFixedDim(): DynamicColor {
        return DynamicColor(
            "tertiary_fixed_dim",
            DynamicScheme.TertiaryPalette,
            { s -> if (s.isMonochrome) 30.0 else 80.0 },
            isBackground = true,
            background = HighestSurface,
            contrastCurve = ContrastCurve.Container,
            toneDeltaPair = { s ->
                ToneDeltaPair(tertiaryFixed(), tertiaryFixedDim(), 10.0, TonePolarity.LIGHTER, true)
            })
    }

    fun onTertiaryFixed(): DynamicColor {
        return DynamicColor(
            "on_tertiary_fixed",
            DynamicScheme.TertiaryPalette,
            { s -> if (s.isMonochrome) 100.0 else 10.0 },
            background = { s -> tertiaryFixedDim() },
            secondBackground = { s -> tertiaryFixed() },
            contrastCurve = ContrastCurve.OnAccent
        )
    }

    fun onTertiaryFixedVariant(): DynamicColor {
        return DynamicColor(
            "on_tertiary_fixed_variant",
            DynamicScheme.TertiaryPalette,
            { s -> if (s.isMonochrome) 90.0 else 30.0 },
            background = { s -> tertiaryFixedDim() },
            secondBackground = { s -> tertiaryFixed() },
            contrastCurve = ContrastCurve.OnContainer
        )
    }

    /**
     * These colors were present in Android framework before Android U, and used by MDC controls. They
     * should be avoided, if possible. It's unclear if they're used on multiple backgrounds, and if
     * they are, they can't be adjusted for contrast.* For now, they will be set with no background,
     * and those won't adjust for contrast, avoiding issues.
     *
     *
     * * For example, if the same color is on a white background _and_ black background, there's no
     * way to increase contrast with either without losing contrast with the other.
     */
    // colorControlActivated documented as colorAccent in M3 & GM3.
    // colorAccent documented as colorSecondary in M3 and colorPrimary in GM3.
    // Android used Material's Container as Primary/Secondary/Tertiary at launch.
    // Therefore, this is a duplicated version of Primary Container.
    fun controlActivated(): DynamicColor {
        return DynamicColor(
            "control_activated",
            DynamicScheme.PrimaryPalette,
            { s -> if (s.isDark) 30.0 else 90.0 })
    }

    // colorControlNormal documented as textColorSecondary in M3 & GM3.
    // In Material, textColorSecondary points to onSurfaceVariant in the non-disabled state,
    // which is Neutral Variant T30/80 in light/dark.
    fun controlNormal(): DynamicColor {
        return DynamicColor(
            "control_normal",
            DynamicScheme.NeutralVariantPalette,
            { s -> if (s.isDark) 80.0 else 30.0 })
    }

    // colorControlHighlight documented, in both M3 & GM3:
    // Light mode: #1f000000 dark mode: #33ffffff.
    // These are black and white with some alpha.
    // 1F hex = 31 decimal; 31 / 255 = 12% alpha.
    // 33 hex = 51 decimal; 51 / 255 = 20% alpha.
    // DynamicColors do not support alpha currently, and _may_ not need it for this use case,
    // depending on how MDC resolved alpha for the other cases.
    // Returning black in dark mode, white in light mode.
    fun controlHighlight(): DynamicColor {
        return DynamicColor(
            "control_highlight",
            DynamicScheme.NeutralPalette,
            { s -> if (s.isDark) 100.0 else 0.0 },
            opacity = { s -> if (s.isDark) 0.20 else 0.12 })
    }

    // textColorPrimaryInverse documented, in both M3 & GM3, documented as N10/N90.
    fun textPrimaryInverse(): DynamicColor {
        return DynamicColor(
            "text_primary_inverse",
            DynamicScheme.NeutralPalette,
            { s -> if (s.isDark) 10.0 else 90.0 })
    }

    // textColorSecondaryInverse and textColorTertiaryInverse both documented, in both M3 & GM3, as
    // NV30/NV80
    fun textSecondaryAndTertiaryInverse(): DynamicColor {
        return DynamicColor(
            "text_secondary_and_tertiary_inverse",
            DynamicScheme.NeutralVariantPalette,
            { s -> if (s.isDark) 30.0 else 80.0 })
    }

    // textColorPrimaryInverseDisableOnly documented, in both M3 & GM3, as N10/N90
    fun textPrimaryInverseDisableOnly(): DynamicColor {
        return DynamicColor(
            "text_primary_inverse_disable_only",
            DynamicScheme.NeutralPalette,
            { s -> if (s.isDark) 10.0 else 90.0 })
    }

    // textColorSecondaryInverse and textColorTertiaryInverse in disabled state both documented,
    // in both M3 & GM3, as N10/N90
    fun textSecondaryAndTertiaryInverseDisabled(): DynamicColor {
        return DynamicColor(
            "text_secondary_and_tertiary_inverse_disabled",
            DynamicScheme.NeutralPalette,
            { s -> if (s.isDark) 10.0 else 90.0 })
    }

    // textColorHintInverse documented, in both M3 & GM3, as N10/N90
    fun textHintInverse(): DynamicColor {
        return DynamicColor(
            "text_hint_inverse",
            DynamicScheme.NeutralPalette,
            { s -> if (s.isDark) 10.0 else 90.0 })
    }

    /** All dynamic colors in Material Design system.  */
    fun allDynamicColors(): List<() -> DynamicColor> {
        return listOf(
            ::primaryPaletteKeyColor,
            ::secondaryPaletteKeyColor,
            ::tertiaryPaletteKeyColor,
            ::neutralPaletteKeyColor,
            ::neutralVariantPaletteKeyColor,
            ::background,
            ::onBackground,
            ::surface,
            ::surfaceDim,
            ::surfaceBright,
            ::surfaceContainerLowest,
            ::surfaceContainerLow,
            ::surfaceContainer,
            ::surfaceContainerHigh,
            ::surfaceContainerHighest,
            ::onSurface,
            ::surfaceVariant,
            ::onSurfaceVariant,
            ::inverseSurface,
            ::inverseOnSurface,
            ::outline,
            ::outlineVariant,
            ::shadow,
            ::scrim,
            ::surfaceTint,
            ::primary,
            ::onPrimary,
            ::primaryContainer,
            ::onPrimaryContainer,
            ::inversePrimary,
            ::secondary,
            ::onSecondary,
            ::secondaryContainer,
            ::onSecondaryContainer,
            ::tertiary,
            ::onTertiary,
            ::tertiaryContainer,
            ::onTertiaryContainer,
            ::error,
            ::onError,
            ::errorContainer,
            ::onErrorContainer,
            ::primaryFixed,
            ::primaryFixedDim,
            ::onPrimaryFixed,
            ::onPrimaryFixedVariant,
            ::secondaryFixed,
            ::secondaryFixedDim,
            ::onSecondaryFixed,
            ::onSecondaryFixedVariant,
            ::tertiaryFixed,
            ::tertiaryFixedDim,
            ::onTertiaryFixed,
            ::onTertiaryFixedVariant,
            ::controlActivated,
            ::controlNormal,
            ::controlHighlight,
            ::textPrimaryInverse,
            ::textSecondaryAndTertiaryInverse,
            ::textPrimaryInverseDisableOnly,
            ::textSecondaryAndTertiaryInverseDisabled,
            ::textHintInverse,
        )
    }

    companion object {
        @JvmStatic
        private fun findDesiredChromaByTone(
            hue: Double,
            chroma: Double,
            tone: Double,
            byDecreasingTone: Boolean
        ): Double {
            var answer = tone

            var closestToChroma = HctSolver.findChroma(hue, chroma, tone)
            if (closestToChroma < chroma) {
                val deltaTone = if (byDecreasingTone) -1.0 else 1.0
                var chromaPeak = closestToChroma
                while (closestToChroma < chroma) {
                    answer += deltaTone
                    val potentialSolution = HctSolver.findChroma(hue, chroma, answer)
                    if (chromaPeak > potentialSolution) {
                        break
                    }
                    if (abs(potentialSolution - chroma) < 0.4) {
                        break
                    }

                    val potentialDelta = abs(potentialSolution - chroma)
                    val currentDelta = abs(closestToChroma - chroma)
                    if (potentialDelta < currentDelta) {
                        closestToChroma = potentialSolution
                    }
                    chromaPeak = potentialSolution
                }
            }

            return answer
        }
    }
}
