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

import com.kyant.aura.core.utils.ColorUtils
import com.kyant.aura.core.utils.MathUtils
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.expm1
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * CAM16, a color appearance model. Colors are not just defined by their hex code, but rather, a hex
 * code and viewing conditions.
 *
 *
 * CAM16 instances also have coordinates in the CAM16-UCS space, called J*, a*, b*, or jstar,
 * astar, bstar in code. CAM16-UCS is included in the CAM16 specification, and should be used when
 * measuring distances between colors.
 *
 *
 * In traditional color spaces, a color can be identified solely by the observer's measurement of
 * the color. Color appearance models such as CAM16 also use information about the environment where
 * the color was observed, known as the viewing conditions.
 *
 *
 * For example, white under the traditional assumption of a midday sun white point is accurately
 * measured as a slightly chromatic blue by CAM16. (roughly, hue 203, chroma 3, lightness 100)
 */
internal class Cam16
/**
 * All the CAM16 dimensions can be calculated from 3 of the dimensions, in the following
 * combinations: - {j or q} and {c, m, or s} and hue - jstar, astar, bstar Prefer using a static
 * method that constructs from 3 of those dimensions. This constructor is intended for those
 * methods to use to return all possible dimensions.
 *
 * @param hue    for example, red, orange, yellow, green, etc.
 * @param chroma informally, colorfulness / color intensity. like saturation in HSL, except
 * perceptually accurate.
 * @param j      lightness
 */ private constructor(
    /**
     * Hue in CAM16
     */
    val hue: Double,
    /**
     * Chroma in CAM16
     */
    val chroma: Double,
    /**
     * Lightness in CAM16
     */
    val j: Double
) {
    companion object {
        const val SCALED_DISCOUNT_FROM_LINRGB_11 = 0.001200833568784504
        const val SCALED_DISCOUNT_FROM_LINRGB_12 = 0.002389694492170889
        const val SCALED_DISCOUNT_FROM_LINRGB_13 = 0.0002795742885861124
        const val SCALED_DISCOUNT_FROM_LINRGB_21 = 0.0005891086651375999
        const val SCALED_DISCOUNT_FROM_LINRGB_22 = 0.0029785502573438758
        const val SCALED_DISCOUNT_FROM_LINRGB_23 = 0.0003270666104008398
        const val SCALED_DISCOUNT_FROM_LINRGB_31 = 0.00010146692491640572
        const val SCALED_DISCOUNT_FROM_LINRGB_32 = 0.0005364214359186694
        const val SCALED_DISCOUNT_FROM_LINRGB_33 = 0.0032979401770712076

        const val LINRGB_FROM_SCALED_DISCOUNT_11 = 1373.2198709594231
        const val LINRGB_FROM_SCALED_DISCOUNT_12 = -1100.4251190754821
        const val LINRGB_FROM_SCALED_DISCOUNT_13 = -7.278681089101213
        const val LINRGB_FROM_SCALED_DISCOUNT_21 = -271.815969077903
        const val LINRGB_FROM_SCALED_DISCOUNT_22 = 559.6580465940733
        const val LINRGB_FROM_SCALED_DISCOUNT_23 = -32.46047482791194
        const val LINRGB_FROM_SCALED_DISCOUNT_31 = 1.9622899599665666
        const val LINRGB_FROM_SCALED_DISCOUNT_32 = -57.173814538844006
        const val LINRGB_FROM_SCALED_DISCOUNT_33 = 308.7233197812385

        /**
         * ARGB representation of the color. Assumes the color was viewed in default viewing conditions,
         * which are near-identical to the default viewing conditions for sRGB.
         */
        @JvmStatic
        fun toInt(j: Double, chroma: Double, hue: Double): Int {
            if (j == 0.0) {
                return 0xFF000000.toInt()
            }

            val vc = DefaultViewingConditions

            val jNormalized = j / 100.0
            val alpha = chroma / sqrt(jNormalized)

            val t = (alpha / vc.alphaCoeff).pow(1.0 / 0.9)
            val hRad = Math.toRadians(hue)

            val eHueScaled = cos(hRad + 2.0) + 3.8
            val p1 = 12500.0 / 13.0 * vc.ncb * eHueScaled
            val p2 = vc.aw / vc.nbb * jNormalized.pow(1.0 / vc.c / vc.z)

            val hSin = sin(hRad)
            val hCos = cos(hRad)

            val gamma = 23.0 * (p2 + 0.305) * t / (23.0 * p1 + 11.0 * t * hCos + 108.0 * t * hSin)
            val a = gamma * hCos
            val b = gamma * hSin
            val rA = (460.0 * p2 + 451.0 * a + 288.0 * b) / 1403.0
            val gA = (460.0 * p2 - 891.0 * a - 261.0 * b) / 1403.0
            val bA = (460.0 * p2 - 220.0 * a - 6300.0 * b) / 1403.0

            val rAAbs = abs(rA)
            val gAAbs = abs(gA)
            val bAAbs = abs(bA)
            val rCBase = max(0.0, (27.13 * rAAbs) / (400.0 - rAAbs))
            val gCBase = max(0.0, (27.13 * gAAbs) / (400.0 - gAAbs))
            val bCBase = max(0.0, (27.13 * bAAbs) / (400.0 - bAAbs))
            val rCScaled = MathUtils.signum(rA) * rCBase.pow(1.0 / 0.42)
            val gCScaled = MathUtils.signum(gA) * gCBase.pow(1.0 / 0.42)
            val bCScaled = MathUtils.signum(bA) * bCBase.pow(1.0 / 0.42)

            val linrgbR =
                rCScaled * LINRGB_FROM_SCALED_DISCOUNT_11 +
                        gCScaled * LINRGB_FROM_SCALED_DISCOUNT_12 +
                        bCScaled * LINRGB_FROM_SCALED_DISCOUNT_13
            val linrgbG =
                rCScaled * LINRGB_FROM_SCALED_DISCOUNT_21 +
                        gCScaled * LINRGB_FROM_SCALED_DISCOUNT_22 +
                        bCScaled * LINRGB_FROM_SCALED_DISCOUNT_23
            val linrgbB =
                rCScaled * LINRGB_FROM_SCALED_DISCOUNT_31 +
                        gCScaled * LINRGB_FROM_SCALED_DISCOUNT_32 +
                        bCScaled * LINRGB_FROM_SCALED_DISCOUNT_33

            return ColorUtils.argbFromLinrgb(linrgbR, linrgbG, linrgbB)
        }

        /**
         * Create a CAM16 color from a color, assuming the color was viewed in default viewing conditions.
         *
         * @param argb ARGB representation of a color.
         */
        @JvmStatic
        fun fromInt(argb: Int): Cam16 {
            // Fast path for black
            if (argb and 0xFFFFFF == 0) {
                return Cam16(0.0, 0.0, 0.0)
            }

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
            val jNormalized = (vc.nbb / vc.aw * p2).pow(vc.c * vc.z)
            val j = 100.0 * jNormalized

            // CAM16 chroma
            val huePrime = if (hue < 20.14) hue + 360 else hue
            val eHueScaled = cos(Math.toRadians(huePrime) + 2.0) + 3.8
            val p1 = 12500.0 / 13.0 * vc.ncb * eHueScaled
            val t = p1 * hypot(a, b) / (u + 0.305)
            val alpha = vc.alphaCoeff * t.pow(0.9)
            val c = alpha * sqrt(jNormalized)

            return Cam16(hue, c, j)
        }

        /**
         * @param j CAM16 lightness
         * @param c CAM16 chroma
         * @param h CAM16 hue
         */
        @JvmStatic
        fun fromJch(j: Double, c: Double, h: Double): Cam16 {
            return Cam16(h, c, j)
        }

        /**
         * Create a CAM16 color from CAM16-UCS coordinates.
         *
         * @param jstar CAM16-UCS lightness.
         * @param astar CAM16-UCS a dimension. Like a* in L*a*b*, it is a Cartesian coordinate on the Y
         * axis.
         * @param bstar CAM16-UCS b dimension. Like a* in L*a*b*, it is a Cartesian coordinate on the X
         * axis.
         */
        @JvmStatic
        fun fromUcs(jstar: Double, astar: Double, bstar: Double): Cam16 {
            val vc = DefaultViewingConditions

            val m = hypot(astar, bstar)
            val m2 = expm1(m * 0.0228) / 0.0228
            val c = m2 / vc.flRoot
            var h = atan2(bstar, astar) * (180.0 / Math.PI)
            if (h < 0.0) {
                h += 360.0
            }
            val j = jstar / (1.0 - (jstar - 100.0) * 0.007)
            return fromJch(j, c, h)
        }
    }
}
