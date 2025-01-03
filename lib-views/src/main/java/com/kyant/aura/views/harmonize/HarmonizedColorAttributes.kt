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
import androidx.annotation.StyleRes
import com.google.android.material.R

/**
 * A class for specifying color attributes for harmonization, which contains an array of color
 * attributes, with the option to specify a custom theme overlay.
 */
class HarmonizedColorAttributes(
    /**
     * Returns the array of color attributes for harmonization.
     */
    @AttrRes val attributes: IntArray,
    /**
     * Returns the custom theme overlay for harmonization, default is 0 if not specified.
     */
    @StyleRes val themeOverlay: Int = 0
) {
    init {
        require(!(themeOverlay != 0 && attributes.isEmpty())) {
            "Theme overlay should be used with the accompanying int[] attributes."
        }
    }

    companion object {
        private val HARMONIZED_MATERIAL_ATTRIBUTES = intArrayOf(
            R.attr.colorError,
            R.attr.colorOnError,
            R.attr.colorErrorContainer,
            R.attr.colorOnErrorContainer
        )

        /**
         * Create [HarmonizedColorAttributes] with Material default, with Error colors being
         * harmonized.
         *
         *
         * Instead of directly overwriting the resources that `colorError`, `colorOnError`,
         * `colorErrorContainer` and `colorOnErrorContainer` points to in the main theme/context, we
         * would:
         *
         *
         *
         * 1. look up the resources values in the theme overlay `Context`.
         * 2. retrieve the harmonized resources with Primary.
         * 3. replace `@color/material_harmonized_color_error`,
         * `@color/material_harmonized_color_on_error`, etc. with the harmonized resources.
         *
         *
         * That way the Error roles in the theme overlay would point to harmonized resources.
         */
        @Suppress("PrivateResource")
        val MaterialDefault = HarmonizedColorAttributes(
            attributes = HARMONIZED_MATERIAL_ATTRIBUTES,
            themeOverlay = R.style.ThemeOverlay_Material3_HarmonizedColors
        )
    }
}
