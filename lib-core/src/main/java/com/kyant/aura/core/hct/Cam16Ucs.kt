package com.kyant.aura.core.hct

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.expm1
import kotlin.math.hypot
import kotlin.math.ln1p
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

internal class Cam16Ucs private constructor(
    // Coordinates in UCS space. Used to determine color distance, like delta E equations in L*a*b*.
    /**
     * J coordinate in CAM16-UCS
     */
    val jstar: Double,
    /**
     * a* coordinate in CAM16-UCS
     */
    val astar: Double,
    /**
     * b* coordinate in CAM16-UCS
     */
    val bstar: Double
) {
    /**
     * CAM16 instances also have coordinates in the CAM16-UCS space, called J*, a*, b*, or jstar,
     * astar, bstar in code. CAM16-UCS is included in the CAM16 specification, and is used to measure
     * distances between colors.
     */
    fun distance(other: Cam16Ucs): Double {
        val dJ = this.jstar - other.jstar
        val dA = this.astar - other.astar
        val dB = this.bstar - other.bstar
        val dEPrime = sqrt(dJ * dJ + dA * dA + dB * dB)
        val dE = 1.41 * dEPrime.pow(0.63)
        return dE
    }

    companion object {
        /**
         *  ARGB representation of the color.
         *
         * @param jstar CAM16-UCS lightness.
         * @param astar CAM16-UCS a dimension. Like a* in L*a*b*, it is a Cartesian coordinate on the Y
         * axis.
         * @param bstar CAM16-UCS b dimension. Like a* in L*a*b*, it is a Cartesian coordinate on the X
         * axis.
         */
        @JvmStatic
        fun toInt(jstar: Double, astar: Double, bstar: Double): Int {
            val vc = DefaultViewingConditions

            val m = hypot(astar, bstar)
            val m2 = expm1(m * 0.0228) / 0.0228
            val c = m2 / vc.flRoot
            var h = atan2(bstar, astar) * (180.0 / Math.PI)
            if (h < 0.0) {
                h += 360.0
            }
            val j = jstar / (1.0 - (jstar - 100.0) * 0.007)
            return Cam16.toInt(j, c, h)
        }

        @JvmStatic
        fun fromInt(argb: Int): Cam16Ucs {
            return fromCam16(Cam16.fromInt(argb))
        }

        @JvmStatic
        fun fromCam16(cam16: Cam16): Cam16Ucs {
            val vc = DefaultViewingConditions

            val j = cam16.j
            val c = cam16.chroma
            val hueRadians = Math.toRadians(cam16.hue)

            val m = c * vc.flRoot

            // CAM16-UCS components
            val jstar = (1.0 + 100.0 * 0.007) * j / (1.0 + 0.007 * j)
            val mstar = 1.0 / 0.0228 * ln1p(0.0228 * m)
            val astar = mstar * cos(hueRadians)
            val bstar = mstar * sin(hueRadians)

            return Cam16Ucs(jstar, astar, bstar)
        }
    }
}
