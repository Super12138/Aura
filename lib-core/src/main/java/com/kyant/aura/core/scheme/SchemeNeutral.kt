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
package com.kyant.aura.core.scheme

import com.kyant.aura.core.dynamiccolor.DynamicScheme
import com.kyant.aura.core.dynamiccolor.Variant
import com.kyant.aura.core.hct.Hct
import com.kyant.aura.core.palettes.TonalPalette

/**
 * A theme that's slightly more chromatic than monochrome, which is purely black / white / gray.
 */
class SchemeNeutral(sourceColorHct: Hct, isDark: Boolean, contrastLevel: Double) : DynamicScheme(
    sourceColorHct = sourceColorHct,
    variant = Variant.NEUTRAL,
    isDark = isDark,
    contrastLevel = contrastLevel,
    primaryPalette = TonalPalette.fromHueAndChroma(sourceColorHct.hue, 12.0),
    secondaryPalette = TonalPalette.fromHueAndChroma(sourceColorHct.hue, 8.0),
    tertiaryPalette = TonalPalette.fromHueAndChroma(sourceColorHct.hue, 16.0),
    neutralPalette = TonalPalette.fromHueAndChroma(sourceColorHct.hue, 2.0),
    neutralVariantPalette = TonalPalette.fromHueAndChroma(sourceColorHct.hue, 2.0)
)
