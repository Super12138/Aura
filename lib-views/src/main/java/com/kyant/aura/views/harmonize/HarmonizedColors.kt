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

import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.SparseIntArray
import android.view.ContextThemeWrapper
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import com.google.android.material.R
import com.kyant.aura.views.MaterialColors.getColor
import com.kyant.aura.views.MaterialColors.harmonize
import com.kyant.aura.views.internal.ResourcesLoaderUtils
import com.kyant.aura.views.internal.ResourcesLoaderUtils.isColorResource
import com.kyant.aura.views.internal.ThemeUtils.applyThemeOverlay

/**
 * A class for harmonizing color resources and attributes.
 *
 *
 * This class is used for harmonizing color resources/attributes defined in xml at runtime. The
 * values of harmonized resources/attributes will be overridden, and afterwards if you retrieve the
 * value from the associated context, or inflate resources like layouts that are using those
 * harmonized resources/attributes, the overridden values will be used instead.
 *
 *
 * If you need to harmonize color resources at runtime, see:
 * [.applyToContextIfAvailable], and
 * [.wrapContextIfAvailable]
 */
object HarmonizedColors {
    private val TAG: String = HarmonizedColors::class.java.getSimpleName()

    /**
     * Harmonizes the specified color resources, attributes, and theme overlay in the [ ] provided.
     *
     *
     * If harmonization is not available, provided color resources and attributes in [ ] will not be harmonized.
     *
     * @param context The target context.
     * @param options The [HarmonizedColorsOptions] object that specifies the resource ids,
     * color attributes to be harmonized and the color attribute to harmonize with.
     */
    @JvmStatic
    @JvmOverloads
    fun applyToContextIfAvailable(
        context: Context,
        options: HarmonizedColorsOptions = HarmonizedColorsOptions.MaterialDefault
    ) {
        if (!isHarmonizedColorAvailable) {
            return
        }
        val colorReplacementMap = createHarmonizedColorReplacementMap(context, options)
        val themeOverlay = options.getThemeOverlayResourceId(0)

        if (ResourcesLoaderUtils.addResourcesLoaderToContext(context, colorReplacementMap) && themeOverlay != 0) {
            applyThemeOverlay(context, themeOverlay)
        }
    }

    /**
     * Wraps the given Context from HarmonizedColorsOptions with the color resources being harmonized.
     *
     *
     * If harmonization is not available, provided color resources and attributes in [ ] will not be harmonized.
     *
     * @param context The target context.
     * @param options The [HarmonizedColorsOptions] object that specifies the resource ids,
     * color attributes to be harmonized and the color attribute to harmonize with.
     * @return the new context with resources being harmonized. If resources are not harmonized
     * successfully, the context passed in will be returned.
     */
    @Suppress("PrivateResource")
    @JvmStatic
    @JvmOverloads
    fun wrapContextIfAvailable(
        context: Context,
        options: HarmonizedColorsOptions = HarmonizedColorsOptions.MaterialDefault
    ): Context {
        if (!isHarmonizedColorAvailable) {
            return context
        }
        // Retrieve colors from original context passed in before the resources are overridden below.
        val colorReplacementMap = createHarmonizedColorReplacementMap(context, options)
        // Empty themeOverlay is used as default to prevent ContextThemeWrapper uses the default theme
        // of the application to wrap Context.
        val themeOverlay = options.getThemeOverlayResourceId(R.style.ThemeOverlay_Material3_HarmonizedColors_Empty)
        val themeWrapper = ContextThemeWrapper(context, themeOverlay)
        // Because ContextThemeWrapper does not provide a new set of resources, override config to
        // retrieve the new set of resources and to keep the original context's resources intact.
        themeWrapper.applyOverrideConfiguration(Configuration())

        return if (ResourcesLoaderUtils.addResourcesLoaderToContext(themeWrapper, colorReplacementMap)) {
            themeWrapper
        } else {
            context
        }
    }

    @get:ChecksSdkIntAtLeast(api = VERSION_CODES.R)
    val isHarmonizedColorAvailable: Boolean
        /**
         *
         * If harmonization is not available, color will not be harmonized.
         *
         * @return `true` if harmonized colors are available on the current SDK level, otherwise
         * `false` will be returned.
         * @see .applyToContextIfAvailable
         * @see .wrapContextIfAvailable
         */
        get() = Build.VERSION.SDK_INT >= VERSION_CODES.R

    @Suppress("ResourceType")
    @RequiresApi(api = VERSION_CODES.R)
    private fun createHarmonizedColorReplacementMap(
        originalContext: Context,
        options: HarmonizedColorsOptions
    ): SparseIntArray {
        val colorReplacementMap = SparseIntArray()
        val colorToHarmonizeWith = originalContext.getColor(options.colorAttributeToHarmonizeWith, TAG)

        // Harmonize color resources.
        for (colorResourceId in options.colorResourceIds) {
            val harmonizedColor = harmonize(originalContext.getColor(colorResourceId), colorToHarmonizeWith)
            colorReplacementMap.put(colorResourceId, harmonizedColor)
        }

        val colorAttributes = options.colorAttributes
        if (colorAttributes != null) {
            val attributes = colorAttributes.attributes
            if (attributes.isNotEmpty()) {
                // Harmonize theme overlay attributes in the custom theme overlay. If custom theme overlay
                // is not provided, look up resources value the theme attributes point to and
                // harmonize directly.
                val themeOverlay = colorAttributes.themeOverlay
                val themeAttributesTypedArray = originalContext.obtainStyledAttributes(attributes)
                val themeOverlayAttributesTypedArray =
                    if (themeOverlay != 0) {
                        ContextThemeWrapper(originalContext, themeOverlay).obtainStyledAttributes(attributes)
                    } else {
                        null
                    }
                addHarmonizedColorAttributesToReplacementMap(
                    colorReplacementMap,
                    themeAttributesTypedArray,
                    themeOverlayAttributesTypedArray,
                    colorToHarmonizeWith
                )

                themeAttributesTypedArray.recycle()
                themeOverlayAttributesTypedArray?.recycle()
            }
        }
        return colorReplacementMap
    }

    @RequiresApi(api = VERSION_CODES.R)
    private fun addHarmonizedColorAttributesToReplacementMap(
        colorReplacementMap: SparseIntArray,
        themeAttributesTypedArray: TypedArray,
        themeOverlayAttributesTypedArray: TypedArray?,
        @ColorInt colorToHarmonizeWith: Int
    ) {
        val resourceIdTypedArray = themeOverlayAttributesTypedArray ?: themeAttributesTypedArray

        for (i in 0..<themeAttributesTypedArray.getIndexCount()) {
            val resourceId = resourceIdTypedArray.getResourceId(i, 0)
            if (resourceId != 0 && themeAttributesTypedArray.hasValue(i)
                && isColorResource(themeAttributesTypedArray.getType(i))
            ) {
                val colorToHarmonize = themeAttributesTypedArray.getColor(i, 0)
                colorReplacementMap.put(resourceId, harmonize(colorToHarmonize, colorToHarmonizeWith))
            }
        }
    }
}
