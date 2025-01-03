/*
 * Copyright 2021 Google LLC
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
package com.kyant.aura.core.hct

import com.kyant.aura.core.hct.Cam16.Companion.SCALED_DISCOUNT_FROM_LINRGB_11
import com.kyant.aura.core.hct.Cam16.Companion.SCALED_DISCOUNT_FROM_LINRGB_12
import com.kyant.aura.core.hct.Cam16.Companion.SCALED_DISCOUNT_FROM_LINRGB_13
import com.kyant.aura.core.hct.Cam16.Companion.SCALED_DISCOUNT_FROM_LINRGB_21
import com.kyant.aura.core.hct.Cam16.Companion.SCALED_DISCOUNT_FROM_LINRGB_22
import com.kyant.aura.core.hct.Cam16.Companion.SCALED_DISCOUNT_FROM_LINRGB_23
import com.kyant.aura.core.hct.Cam16.Companion.SCALED_DISCOUNT_FROM_LINRGB_31
import com.kyant.aura.core.hct.Cam16.Companion.SCALED_DISCOUNT_FROM_LINRGB_32
import com.kyant.aura.core.hct.Cam16.Companion.SCALED_DISCOUNT_FROM_LINRGB_33
import com.kyant.aura.core.utils.ColorUtils
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.pow

/**
 * A color system built using CAM16 hue and chroma, and L* from L*a*b*.
 *
 *
 * Using L* creates a link between the color system, contrast, and thus accessibility. Contrast
 * ratio depends on relative luminance, or Y in the XYZ color space. L*, or perceptual luminance can
 * be calculated from Y.
 *
 *
 * Unlike Y, L* is linear to human perception, allowing trivial creation of accurate color tones.
 *
 *
 * Unlike contrast ratio, measuring contrast in L* is linear, and simple to calculate. A
 * difference of 40 in HCT tone guarantees a contrast ratio >= 3.0, and a difference of 50
 * guarantees a contrast ratio >= 4.5.
 */
/**
 * HCT, hue, chroma, and tone. A color system that provides a perceptually accurate color
 * measurement system that can also accurately render what colors will appear as in different
 * lighting environments.
 */
class Hct
/**
 * Create an HCT color from a color.
 *
 * @param argb ARGB representation of a color.
 * @return HCT representation of a color in default viewing conditions
 */ constructor(private val argb: Int) {
    val hue: Double
    val chroma: Double
    val tone: Double

    init {
        // ===========================================================
        // Operations inlined from Cam16 to avoid repeated calculation
        // ===========================================================
        val vc = DefaultViewingConditions

        val linrgbR = ColorUtils.linearized(argb shr 16 and 0xFF)
        val linrgbG = ColorUtils.linearized(argb shr 8 and 0xFF)
        val linrgbB = ColorUtils.linearized(argb and 0xFF)

        // Transform XYZ to scaled discounted illuminant
        val rDScaled =
            linrgbR * SCALED_DISCOUNT_FROM_LINRGB_11 +
                    linrgbG * SCALED_DISCOUNT_FROM_LINRGB_12 +
                    linrgbB * SCALED_DISCOUNT_FROM_LINRGB_13
        val gDScaled =
            linrgbR * SCALED_DISCOUNT_FROM_LINRGB_21 +
                    linrgbG * SCALED_DISCOUNT_FROM_LINRGB_22 +
                    linrgbB * SCALED_DISCOUNT_FROM_LINRGB_23
        val bDScaled =
            linrgbR * SCALED_DISCOUNT_FROM_LINRGB_31 +
                    linrgbG * SCALED_DISCOUNT_FROM_LINRGB_32 +
                    linrgbB * SCALED_DISCOUNT_FROM_LINRGB_33

        // Chromatic adaptation
        val rAF = rDScaled.pow(0.42)
        val gAF = gDScaled.pow(0.42)
        val bAF = bDScaled.pow(0.42)
        val rA = 400.0 * rAF / (rAF + 27.13)
        val gA = 400.0 * gAF / (gAF + 27.13)
        val bA = 400.0 * bAF / (bAF + 27.13)

        // redness-greenness
        val a = rA - gA + (bA - gA) / 11.0
        // yellowness-blueness
        val b = (rA + gA - bA - bA) / 9.0

        // auxiliary components
        val u = rA + gA + bA + bA / 20.0
        val p2 = rA + rA + gA + bA / 20.0

        // hue
        val atan2 = atan2(b, a)
        val atanDegrees = Math.toDegrees(atan2)
        val hue =
            if (atanDegrees < 0) {
                atanDegrees + 360.0
            } else {
                if (atanDegrees >= 360) {
                    atanDegrees - 360.0
                } else {
                    atanDegrees
                }
            }

        // CAM16 lightness
        val jNormalizedSqrt = (vc.nbb / vc.aw * p2).pow(vc.c * vc.z / 2.0)

        // CAM16 chroma
        val huePrime = if (hue < 20.14) hue + 360 else hue
        val eHueScaled = cos(Math.toRadians(huePrime) + 2.0) + 3.8
        val p1 = 12500.0 / 13.0 * vc.ncb * eHueScaled
        val t = p1 * hypot(a, b) / (u + 0.305)
        val alpha = vc.alphaCoeff * t.pow(0.9)
        val c = alpha * jNormalizedSqrt
        // ===========================================================
        // Operations inlined from Cam16 to avoid repeated calculation
        // ===========================================================

        this.hue = hue
        this.chroma = c
        this.tone = ColorUtils.lstarFromArgb(argb)
    }

    /**
     * Create an HCT color from hue, chroma, and tone.
     *
     * @param hue    0 <= hue < 360; invalid values are corrected.
     * @param chroma 0 <= chroma < ?; Informally, colorfulness. The color returned may be lower than
     * the requested chroma. Chroma has a different maximum for any given hue and tone.
     * @param tone   0 <= tone <= 100; invalid values are corrected.
     * @return HCT representation of a color in default viewing conditions.
     */
    constructor(hue: Double, chroma: Double, tone: Double) : this(HctSolver.solveToInt(hue, chroma, tone))

    fun asArgb(): Int {
        return argb
    }

    fun copy(
        hue: Double = this.hue,
        chroma: Double = this.chroma,
        tone: Double = this.tone
    ): Hct {
        return Hct(hue, chroma, tone)
    }

    override fun toString(): String {
        return "Hct(hue=$hue, chroma=$chroma, tone=$tone)"
    }

    companion object {
        @JvmStatic
        fun hueOf(argb: Int): Double {
            // ===========================================================
            // Operations inlined from Cam16 to avoid repeated calculation
            // ===========================================================
            val vc = DefaultViewingConditions

            val linrgbR = ColorUtils.linearized(argb shr 16 and 0xFF)
            val linrgbG = ColorUtils.linearized(argb shr 8 and 0xFF)
            val linrgbB = ColorUtils.linearized(argb and 0xFF)

            // Transform XYZ to scaled discounted illuminant
            val rDScaled =
                linrgbR * SCALED_DISCOUNT_FROM_LINRGB_11 +
                        linrgbG * SCALED_DISCOUNT_FROM_LINRGB_12 +
                        linrgbB * SCALED_DISCOUNT_FROM_LINRGB_13
            val gDScaled =
                linrgbR * SCALED_DISCOUNT_FROM_LINRGB_21 +
                        linrgbG * SCALED_DISCOUNT_FROM_LINRGB_22 +
                        linrgbB * SCALED_DISCOUNT_FROM_LINRGB_23
            val bDScaled =
                linrgbR * SCALED_DISCOUNT_FROM_LINRGB_31 +
                        linrgbG * SCALED_DISCOUNT_FROM_LINRGB_32 +
                        linrgbB * SCALED_DISCOUNT_FROM_LINRGB_33

            // Chromatic adaptation
            val rAF = rDScaled.pow(0.42)
            val gAF = gDScaled.pow(0.42)
            val bAF = bDScaled.pow(0.42)
            val rA = 400.0 * rAF / (rAF + 27.13)
            val gA = 400.0 * gAF / (gAF + 27.13)
            val bA = 400.0 * bAF / (bAF + 27.13)

            // redness-greenness
            val a = rA - gA + (bA - gA) / 11.0
            // yellowness-blueness
            val b = (rA + gA - bA - bA) / 9.0

            // hue
            val atan2 = atan2(b, a)
            val atanDegrees = Math.toDegrees(atan2)
            val hue =
                if (atanDegrees < 0) {
                    atanDegrees + 360.0
                } else {
                    if (atanDegrees >= 360) {
                        atanDegrees - 360.0
                    } else {
                        atanDegrees
                    }
                }
            // ===========================================================
            // Operations inlined from Cam16 to avoid repeated calculation
            // ===========================================================

            return hue
        }
    }
}
