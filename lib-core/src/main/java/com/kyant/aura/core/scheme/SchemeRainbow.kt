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
package com.kyant.aura.core.scheme

import com.kyant.aura.core.dynamiccolor.DynamicScheme
import com.kyant.aura.core.dynamiccolor.Variant
import com.kyant.aura.core.hct.Hct
import com.kyant.aura.core.palettes.TonalPalette
import com.kyant.aura.core.utils.MathUtils

/**
 * A playful theme - the source color's hue does not appear in the theme.
 */
class SchemeRainbow(sourceColorHct: Hct, isDark: Boolean, contrastLevel: Double) : DynamicScheme(
    sourceColorHct = sourceColorHct,
    variant = Variant.RAINBOW,
    isDark = isDark,
    contrastLevel = contrastLevel,
    primaryPalette = TonalPalette.fromHueAndChroma(sourceColorHct.hue, 48.0),
    secondaryPalette = TonalPalette.fromHueAndChroma(sourceColorHct.hue, 16.0),
    tertiaryPalette = TonalPalette.fromHueAndChroma(MathUtils.sanitizeDegreesDouble(sourceColorHct.hue + 60.0), 24.0),
    neutralPalette = TonalPalette.fromHueAndChroma(sourceColorHct.hue, 0.0),
    neutralVariantPalette = TonalPalette.fromHueAndChroma(sourceColorHct.hue, 0.0)
)
