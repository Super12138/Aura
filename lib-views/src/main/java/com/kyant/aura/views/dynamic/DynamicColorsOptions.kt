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
package com.kyant.aura.views.dynamic

import android.app.Activity
import androidx.annotation.FloatRange
import androidx.annotation.StyleRes
import com.kyant.aura.core.dynamiccolor.Variant

/**
 * Wrapper class for specifying dynamic colors options when applying dynamic colors. Clients have
 * the options to provide a custom theme overlay, set the precondition that decides if dynamic
 * colors should be applied, set the callback method and/or set the color source image to apply
 * content-based dynamic colors.
 */
class DynamicColorsOptions(
    /** The resource ID of the theme overlay that provides dynamic color definition. */
    @StyleRes
    val themeOverlay: Int = 0,
    /** The precondition that decides if dynamic colors should be applied. */
    val precondition: Precondition = ALWAYS_ALLOW,
    /** The callback method after dynamic colors have been applied. */
    val onAppliedCallback: OnAppliedCallback = NO_OP_CALLBACK,
    val dynamicColorSource: DynamicColorSource? = null,
    val variant: Variant = Variant.CONTENT,
    @FloatRange(-1.0, 1.0)
    val contrastLevel: Double = Double.NaN
) {
    /** Returns the seed color extracted from the color source image.  */
    val seedColor: Int? = dynamicColorSource?.seedColor

    companion object {
        val Default = DynamicColorsOptions()

        /** The interface that provides a precondition to decide if dynamic colors should be applied.  */
        fun interface Precondition {
            /**
             * Return `true` if dynamic colors should be applied on the given activity with the given
             * theme overlay.
             */
            fun shouldApplyDynamicColors(activity: Activity, @StyleRes theme: Int): Boolean
        }

        /** The interface that provides a callback method after dynamic colors have been applied.  */
        fun interface OnAppliedCallback {
            /** The callback method after dynamic colors have been applied.  */
            fun onApplied(activity: Activity)
        }

        private val ALWAYS_ALLOW = Precondition { activity, theme ->
            true
        }

        private val NO_OP_CALLBACK = OnAppliedCallback {
        }
    }
}
