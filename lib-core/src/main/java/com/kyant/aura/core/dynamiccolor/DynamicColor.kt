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

import com.kyant.aura.core.contrast.Contrast
import com.kyant.aura.core.hct.Hct
import com.kyant.aura.core.palettes.TonalPalette
import com.kyant.aura.core.utils.MathUtils
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * A color that adjusts itself based on UI state, represented by DynamicScheme.
 *
 *
 * This color automatically adjusts to accommodate a desired contrast level, or other adjustments
 * such as differing in light mode versus dark mode, or what the theme is, or what the color that
 * produced the theme is, etc.
 *
 *
 * Colors without backgrounds do not change tone when contrast changes. Colors with backgrounds
 * become closer to their background as contrast lowers, and further when contrast increases.
 *
 *
 * Prefer the static constructors. They provide a much more simple interface, such as requiring
 * just a hexcode, or just a hexcode and a background.
 *
 *
 * Ultimately, each component necessary for calculating a color, adjusting it for a desired
 * contrast level, and ensuring it has a certain lightness/tone difference from another color, is
 * provided by a function that takes a DynamicScheme and returns a value. This ensures ultimate
 * flexibility, any desired behavior of a color for any design system, but it is usually unnecessary.
 * See the default constructor for more information.
 */
// Prevent lint for Function.apply not being available on Android before API level 14 (4.0.1).
// "AndroidJdkLibsChecker" for Function, "NewApi" for Function.apply().
// A java_library Bazel rule with an Android constraint cannot skip these warnings without this
// annotation; another solution would be to create an android_library rule and supply
// AndroidManifest with an SDK set higher than 14.
class DynamicColor
/**
 * A constructor for DynamicColor.
 *
 *
 * _Strongly_ prefer using one of the convenience constructors. This class is arguably too
 * flexible to ensure it can support any scenario. Functional arguments allow overriding without
 * risks that come with subclasses.
 *
 *
 * For example, the default behavior of adjust tone at max contrast to be at a 7.0 ratio with
 * its background is principled and matches accessibility guidance. That does not mean it's the
 * desired approach for _every_ design system, and every color pairing, always, in every case.
 *
 *
 * For opaque colors (colors with alpha = 100%).
 *
 * @param name             The name of the dynamic color.
 * @param palette          Function that provides a TonalPalette given DynamicScheme. A TonalPalette is
 * defined by a hue and chroma, so this replaces the need to specify hue/chroma. By providing
 * a tonal palette, when contrast adjustments are made, intended chroma can be preserved.
 * @param tone             Function that provides a tone, given a DynamicScheme.
 * @param isBackground     Whether this dynamic color is a background, with some other color as the
 * foreground.
 * @param background       The background of the dynamic color (as a function of a `DynamicScheme`), if
 * it exists.
 * @param secondBackground A second background of the dynamic color (as a function of a
 * `DynamicScheme`), if it exists.
 * @param contrastCurve    A `ContrastCurve` object specifying how its contrast against its
 * background should behave in various contrast levels options.
 * @param toneDeltaPair    A `ToneDeltaPair` object specifying a tone delta constraint between two
 * colors. One of them must be the color being constructed.
 * @param opacity          A function returning the opacity of a color, as a number between 0 and 1.
 */(
    val name: String,
    val palette: (DynamicScheme) -> TonalPalette,
    val tone: (DynamicScheme) -> Double,
    val isBackground: Boolean = false,
    val background: ((DynamicScheme) -> DynamicColor)? = null,
    val secondBackground: ((DynamicScheme) -> DynamicColor)? = null,
    val contrastCurve: ContrastCurve? = null,
    val toneDeltaPair: ((DynamicScheme) -> ToneDeltaPair)? = null,
    val opacity: ((DynamicScheme) -> Double)? = null
) {
    /**
     * Returns an ARGB integer (i.e. a hex code).
     *
     * @param scheme Defines the conditions of the user interface, for example, whether it is
     * dark mode or light mode, and what the desired contrast level is.
     */
    fun getArgb(scheme: DynamicScheme): Int {
        val tone = getTone(scheme)
        val argb = palette(scheme).getArgb(tone)
        if (opacity == null) {
            return argb
        }
        val percentage = opacity(scheme)
        val alpha = MathUtils.clampInt(0, 255, (percentage * 255).roundToInt())
        return (argb and 0x00FFFFFF) or (alpha shl 24)
    }

    /**
     * Returns an HCT object.
     *
     * @param scheme Defines the conditions of the user interface, for example, whether it is
     * dark mode or light mode, and what the desired contrast level is.
     */
    fun getHct(scheme: DynamicScheme): Hct {
        // This is crucial for aesthetics: we aren't simply the taking the standard color
        // and changing its tone for contrast. Rather, we find the tone for contrast, then
        // use the specified chroma from the palette to construct a new color.
        //
        // For example, this enables colors with standard tone of T90, which has limited chroma, to
        // "recover" intended chroma as contrast increases.
        val tone = getTone(scheme)
        return palette(scheme).getHct(tone)
    }

    /**
     * Returns the tone in HCT, ranging from 0 to 100, of the resolved color given scheme.
     */
    fun getTone(scheme: DynamicScheme): Double {
        val decreasingContrast = scheme.contrastLevel < 0

        // Case 1: dual foreground, a pair of colors with delta constraint.
        if (toneDeltaPair != null) {
            val toneDeltaPair = toneDeltaPair(scheme)
            val roleA = toneDeltaPair.roleA
            val roleB = toneDeltaPair.roleB
            val delta = toneDeltaPair.delta
            val polarity = toneDeltaPair.polarity
            val stayTogether = toneDeltaPair.stayTogether

            val bg = background!!(scheme)
            val bgTone = bg.getTone(scheme)

            val aIsNearer =
                (polarity == TonePolarity.NEARER
                        || (polarity == TonePolarity.LIGHTER && !scheme.isDark)
                        || (polarity == TonePolarity.DARKER && scheme.isDark))
            val nearer = if (aIsNearer) roleA else roleB
            val farther = if (aIsNearer) roleB else roleA
            val amNearer = name == nearer.name
            val expansionDir = if (scheme.isDark) 1.0 else -1.0

            // 1st round: solve to min, each
            val nContrast = nearer.contrastCurve!!.get(scheme.contrastLevel)
            val fContrast = farther.contrastCurve!!.get(scheme.contrastLevel)

            // If a color is good enough, it is not adjusted.
            // Initial and adjusted tones for `nearer`
            val nInitialTone = nearer.tone(scheme)
            var nTone =
                if (Contrast.ratioOfTones(bgTone, nInitialTone) >= nContrast) {
                    nInitialTone
                } else {
                    foregroundTone(bgTone, nContrast)
                }
            // Initial and adjusted tones for `farther`
            val fInitialTone = farther.tone(scheme)
            var fTone =
                if (Contrast.ratioOfTones(bgTone, fInitialTone) >= fContrast) {
                    fInitialTone
                } else {
                    foregroundTone(bgTone, fContrast)
                }

            if (decreasingContrast) {
                // If decreasing contrast, adjust color to the "bare minimum"
                // that satisfies contrast.
                nTone = foregroundTone(bgTone, nContrast)
                fTone = foregroundTone(bgTone, fContrast)
            }

            // If constraint is not satisfied, try another round.
            if ((fTone - nTone) * expansionDir < delta) {
                // 2nd round: expand farther to match delta.
                fTone = MathUtils.clampDouble(0.0, 100.0, nTone + delta * expansionDir)
                // If constraint is not satisfied, try another round.
                if ((fTone - nTone) * expansionDir < delta) {
                    // 3rd round: contract nearer to match delta.
                    nTone = MathUtils.clampDouble(0.0, 100.0, fTone - delta * expansionDir)
                }
            }

            // Avoids the 50-59 awkward zone.
            if (50 <= nTone && nTone < 60) {
                // If `nearer` is in the awkward zone, move it away, together with
                // `farther`.
                if (expansionDir > 0) {
                    nTone = 60.0
                    fTone = max(fTone, nTone + delta * expansionDir)
                } else {
                    nTone = 49.0
                    fTone = min(fTone, nTone + delta * expansionDir)
                }
            } else if (50 <= fTone && fTone < 60) {
                if (stayTogether) {
                    // Fixes both, to avoid two colors on opposite sides of the "awkward
                    // zone".
                    if (expansionDir > 0) {
                        nTone = 60.0
                        fTone = max(fTone, nTone + delta * expansionDir)
                    } else {
                        nTone = 49.0
                        fTone = min(fTone, nTone + delta * expansionDir)
                    }
                } else {
                    // Not required to stay together; fixes just one.
                    fTone = if (expansionDir > 0) {
                        60.0
                    } else {
                        49.0
                    }
                }
            }

            // Returns `nTone` if this color is `nearer`, otherwise `fTone`.
            return if (amNearer) nTone else fTone
        } else {
            // Case 2: No contrast pair; just solve for itself.
            var answer = tone(scheme)

            if (background == null) {
                return answer // No adjustment for colors with no background.
            }

            val bgTone = background(scheme).getTone(scheme)

            val desiredRatio = contrastCurve!!.get(scheme.contrastLevel)

            if (Contrast.ratioOfTones(bgTone, answer) >= desiredRatio) {
                // Don't "improve" what's good enough.
            } else {
                // Rough improvement.
                answer = foregroundTone(bgTone, desiredRatio)
            }

            if (decreasingContrast) {
                answer = foregroundTone(bgTone, desiredRatio)
            }

            if (isBackground && 50 <= answer && answer < 60) {
                // Must adjust
                answer = if (Contrast.ratioOfTones(49.0, bgTone) >= desiredRatio) {
                    49.0
                } else {
                    60.0
                }
            }

            if (secondBackground != null) {
                // Case 3: Adjust for dual backgrounds.

                val bgTone1 = background(scheme).getTone(scheme)
                val bgTone2 = secondBackground(scheme).getTone(scheme)

                val upper = max(bgTone1, bgTone2)
                val lower = min(bgTone1, bgTone2)

                if (Contrast.ratioOfTones(upper, answer) >= desiredRatio
                    && Contrast.ratioOfTones(lower, answer) >= desiredRatio
                ) {
                    return answer
                }

                // The darkest light tone that satisfies the desired ratio,
                // or -1 if such ratio cannot be reached.
                val lightOption = Contrast.lighter(upper, desiredRatio)

                // The lightest dark tone that satisfies the desired ratio,
                // or -1 if such ratio cannot be reached.
                val darkOption = Contrast.darker(lower, desiredRatio)

                // Tones suitable for the foreground.
                val availables = ArrayList<Double>()
                if (lightOption != -1.0) {
                    availables.add(lightOption)
                }
                if (darkOption != -1.0) {
                    availables.add(darkOption)
                }

                val prefersLight = tonePrefersLightForeground(bgTone1) || tonePrefersLightForeground(bgTone2)
                if (prefersLight) {
                    return if (lightOption == -1.0) 100.0 else lightOption
                }
                if (availables.size == 1) {
                    return availables[0]
                }
                return if (darkOption == -1.0) 0.0 else darkOption
            }

            return answer
        }
    }

    companion object {
        /**
         * Create a DynamicColor from a hex code.
         *
         *
         * Result has no background; thus no support for increasing/decreasing contrast for a11y.
         *
         * @param name The name of the dynamic color.
         * @param argb The source color from which to extract the hue and chroma.
         */
        @JvmStatic
        fun fromArgb(name: String, argb: Int): DynamicColor {
            val hct = Hct(argb)
            val palette = TonalPalette.fromInt(argb)
            return DynamicColor(
                name = name,
                palette = { palette },
                tone = { hct.tone })
        }

        /**
         * Given a background tone, find a foreground tone, while ensuring they reach a contrast ratio
         * that is as close to ratio as possible.
         */
        @JvmStatic
        fun foregroundTone(bgTone: Double, ratio: Double): Double {
            val lighterTone = Contrast.lighterUnsafe(bgTone, ratio)
            val darkerTone = Contrast.darkerUnsafe(bgTone, ratio)
            val lighterRatio = Contrast.ratioOfTones(lighterTone, bgTone)
            val darkerRatio = Contrast.ratioOfTones(darkerTone, bgTone)
            val preferLighter = tonePrefersLightForeground(bgTone)

            if (preferLighter) {
                // "Neglible difference" handles an edge case where the initial contrast ratio is high
                // (ex. 13.0), and the ratio passed to the function is that high ratio, and both the lighter
                // and darker ratio fails to pass that ratio.
                //
                // This was observed with Tonal Spot's On Primary Container turning black momentarily between
                // high and max contrast in light mode. PC's standard tone was T90, OPC's was T10, it was
                // light mode, and the contrast level was 0.6568521221032331.
                val negligibleDifference =
                    abs(lighterRatio - darkerRatio) < 0.1 && lighterRatio < ratio && darkerRatio < ratio
                return if (lighterRatio >= ratio || lighterRatio >= darkerRatio || negligibleDifference) {
                    lighterTone
                } else {
                    darkerTone
                }
            } else {
                return if (darkerRatio >= ratio || darkerRatio >= lighterRatio) darkerTone else lighterTone
            }
        }

        /**
         * Adjust a tone down such that white has 4.5 contrast, if the tone is reasonably close to
         * supporting it.
         */
        @Suppress("NOTHING_TO_INLINE")
        @JvmStatic
        inline fun enableLightForeground(tone: Double): Double {
            if (tonePrefersLightForeground(tone) && !toneAllowsLightForeground(tone)) {
                return 49.0
            }
            return tone
        }

        /**
         * People prefer white foregrounds on ~T60-70. Observed over time, and also by Andrew Somers
         * during research for APCA.
         *
         *
         * T60 used as to create the smallest discontinuity possible when skipping down to T49 in order
         * to ensure light foregrounds.
         *
         *
         * Since `tertiaryContainer` in dark monochrome scheme requires a tone of 60, it should not be
         * adjusted. Therefore, 60 is excluded here.
         */
        @Suppress("NOTHING_TO_INLINE")
        @JvmStatic
        inline fun tonePrefersLightForeground(tone: Double): Boolean {
            return tone < 59.5
        }

        /**
         * Tones less than ~T50 always permit white at 4.5 contrast.
         */
        @Suppress("NOTHING_TO_INLINE")
        @JvmStatic
        inline fun toneAllowsLightForeground(tone: Double): Boolean {
            return tone <= 49.5
        }
    }
}
