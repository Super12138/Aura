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

import com.kyant.aura.core.utils.MathUtils

/**
 * A class containing a value that changes with the contrast level.
 *
 *
 * Usually represents the contrast requirements for a dynamic color on its background. The four
 * values correspond to values for contrast levels -1.0, 0.0, 0.5, and 1.0, respectively.
 */
class ContrastCurve
/**
 * Creates a `ContrastCurve` object.
 *
 * @param low    Value for contrast level -1.0
 * @param normal Value for contrast level 0.0
 * @param medium Value for contrast level 0.5
 * @param high   Value for contrast level 1.0
 */(
    /**
     * Value for contrast level -1.0
     */
    private val low: Double,
    /**
     * Value for contrast level 0.0
     */
    private val normal: Double,
    /**
     * Value for contrast level 0.5
     */
    private val medium: Double,
    /**
     * Value for contrast level 1.0
     */
    private val high: Double
) {
    /**
     * Returns the value at a given contrast level.
     *
     * @param contrastLevel The contrast level. 0.0 is the default (normal); -1.0 is the lowest; 1.0
     * is the highest.
     * @return The value. For contrast ratios, a number between 1.0 and 21.0.
     */
    fun get(contrastLevel: Double): Double {
        return when {
            contrastLevel <= -1.0 -> this.low
            contrastLevel < 0.0 -> MathUtils.lerp(this.low, this.normal, contrastLevel + 1)
            contrastLevel < 0.5 -> MathUtils.lerp(this.normal, this.medium, contrastLevel * 2)
            contrastLevel < 1.0 -> MathUtils.lerp(this.medium, this.high, contrastLevel * 2 - 1)
            else -> this.high
        }
    }

    companion object {
        val Accent = ContrastCurve(3.0, 4.5, 7.0, 7.0)
        val OnAccent = ContrastCurve(4.5, 7.0, 11.0, 21.0)
        val Container = ContrastCurve(1.0, 1.0, 3.0, 4.5)
        val OnContainer = ContrastCurve(3.0, 4.5, 7.0, 11.0)

        /**
         * Returns the value at a given contrast level.
         *
         * @param contrastLevel The contrast level. 0.0 is the default (normal); -1.0 is the lowest; 1.0
         * is the highest.
         * @return The value. For contrast ratios, a number between 1.0 and 21.0.
         */
        @JvmStatic
        fun get(
            low: Double,
            normal: Double,
            medium: Double,
            high: Double,
            contrastLevel: Double
        ): Double {
            return when {
                contrastLevel <= -1.0 -> low
                contrastLevel < 0.0 -> MathUtils.lerp(low, normal, contrastLevel + 1)
                contrastLevel < 0.5 -> MathUtils.lerp(normal, medium, contrastLevel * 2)
                contrastLevel < 1.0 -> MathUtils.lerp(medium, high, contrastLevel * 2 - 1)
                else -> high
            }
        }
    }
}
