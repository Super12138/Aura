/*
 * Copyright (C) 2022 The Android Open Source Project
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
package com.kyant.aura.views.harmonize

import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import com.google.android.material.R

/**
 * Wrapper class for specifying harmonization options, whether to harmonize an array of color
 * resources, or a [HarmonizedColorAttributes], along with the color attribute provided to
 * harmonize with.
 */
class HarmonizedColorsOptions(
    /**
     * The array of color resource ids that needs to be harmonized.
     */
    @ColorRes
    val colorResourceIds: IntArray = intArrayOf(),
    /**
     * The [HarmonizedColorAttributes] that needs to be harmonized.
     */
    val colorAttributes: HarmonizedColorAttributes? = null,
    /**
     * The color attribute to harmonize color resources and [HarmonizedColorAttributes] with.
     */
    @AttrRes
    val colorAttributeToHarmonizeWith: Int = R.attr.colorPrimary
) {
    @StyleRes
    fun getThemeOverlayResourceId(@StyleRes defaultThemeOverlay: Int): Int {
        return if (colorAttributes != null && colorAttributes.themeOverlay != 0) {
            colorAttributes.themeOverlay
        } else {
            defaultThemeOverlay
        }
    }

    companion object {
        /**
         * Create HarmonizedColorsOptions with Material default, with Error colors being harmonized with
         * Primary.
         */
        val MaterialDefault = HarmonizedColorsOptions(colorAttributes = HarmonizedColorAttributes.MaterialDefault)
    }
}
