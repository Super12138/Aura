/*
 * Copyright (C) 2018 The Android Open Source Project
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
package com.kyant.aura.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.google.android.material.R
import com.kyant.aura.core.blend.Blend
import com.kyant.aura.core.hct.Hct
import com.kyant.aura.views.MaterialColors.layer
import com.kyant.aura.views.dynamic.MaterialColorRoles
import com.kyant.aura.views.internal.AttributesUtils
import kotlin.math.roundToInt

/**
 * A utility class for common color variants used in Material themes.
 */
@Suppress("unused")
object MaterialColors {
    const val ALPHA_FULL: Float = 1.00f
    const val ALPHA_MEDIUM: Float = 0.54f
    const val ALPHA_DISABLED: Float = 0.38f
    const val ALPHA_LOW: Float = 0.32f
    const val ALPHA_DISABLED_LOW: Float = 0.12f

    // TODO(b/199495444): token integration for color roles luminance values.
    // Tone means degrees of lightness, in the range of 0 (inclusive) to 100 (inclusive).
    // Spec: https://m3.material.io/styles/color/the-color-system/color-roles
    private const val TONE_ACCENT_LIGHT = 40
    private const val TONE_ON_ACCENT_LIGHT = 100
    private const val TONE_ACCENT_CONTAINER_LIGHT = 90
    private const val TONE_ON_ACCENT_CONTAINER_LIGHT = 10
    private const val TONE_SURFACE_CONTAINER_LIGHT = 94
    private const val TONE_SURFACE_CONTAINER_HIGH_LIGHT = 92
    private const val TONE_ACCENT_DARK = 80
    private const val TONE_ON_ACCENT_DARK = 20
    private const val TONE_ACCENT_CONTAINER_DARK = 30
    private const val TONE_ON_ACCENT_CONTAINER_DARK = 90
    private const val TONE_SURFACE_CONTAINER_DARK = 12
    private const val TONE_SURFACE_CONTAINER_HIGH_DARK = 17
    private const val CHROMA_NEUTRAL = 6

    @ColorInt
    fun View.getMaterialColor(
        colorRole: MaterialColorRoles,
        @ColorInt defaultValue: Int = 0
    ): Int {
        return context.getMaterialColor(colorRole, defaultValue)
    }

    @ColorInt
    fun Context.getMaterialColor(
        colorRole: MaterialColorRoles,
        @ColorInt defaultValue: Int = 0
    ): Int {
        val typedValue = TypedValue()
        return if (theme.resolveAttribute(colorRole.resId, typedValue, true)) {
            if (typedValue.resourceId != 0) {
                // Color State List
                getColor(typedValue.resourceId)
            } else {
                // Color Int
                typedValue.data
            }
        } else {
            defaultValue
        }
    }

    /**
     * Returns the color int for the provided theme color attribute, using the [Context] of the
     * provided `view`.
     *
     * @throws IllegalArgumentException if the attribute is not set in the current theme.
     */
    @ColorInt
    fun View.getColor(@AttrRes colorAttributeResId: Int): Int {
        return context.resolveColor(
            AttributesUtils.resolveTypedValueOrThrow(this, colorAttributeResId)
        )
    }

    /**
     * Returns the color int for the provided theme color attribute.
     *
     * @throws IllegalArgumentException if the attribute is not set in the current theme.
     */
    @ColorInt
    fun Context.getColor(@AttrRes colorAttributeResId: Int, errorMessageComponent: String?): Int {
        return resolveColor(
            AttributesUtils.resolveTypedValueOrThrow(this, colorAttributeResId, errorMessageComponent)
        )
    }

    /**
     * Returns the color int for the provided theme color attribute, or the default value if the
     * attribute is not set in the current theme, using the `view`'s [Context].
     */
    @ColorInt
    fun View.getColor(@AttrRes colorAttributeResId: Int, @ColorInt defaultValue: Int): Int {
        return context.getColor(colorAttributeResId, defaultValue)
    }

    /**
     * Returns the color int for the provided theme color attribute, or the default value if the
     * attribute is not set in the current theme.
     */
    @ColorInt
    fun Context.getColor(@AttrRes colorAttributeResId: Int, @ColorInt defaultValue: Int): Int {
        return getColorOrNull(colorAttributeResId) ?: defaultValue
    }

    /**
     * Returns the color int for the provided theme color attribute, or null if the attribute is not
     * set in the current theme.
     */
    @ColorInt
    fun Context.getColorOrNull(@AttrRes colorAttributeResId: Int): Int? {
        val typedValue = AttributesUtils.resolve(this, colorAttributeResId)
        return if (typedValue != null) resolveColor(typedValue) else null
    }

    /**
     * Returns the color state list for the provided theme color attribute, or the default value if
     * the attribute is not set in the current theme.
     */
    fun Context.getColorStateList(@AttrRes colorAttributeResId: Int, defaultValue: ColorStateList): ColorStateList {
        var resolvedColor: ColorStateList? = null
        val typedValue = AttributesUtils.resolve(this, colorAttributeResId)
        if (typedValue != null) {
            resolvedColor = resolveColorStateList(typedValue)
        }
        return resolvedColor ?: defaultValue
    }

    /**
     * Returns the color state list for the provided theme color attribute, or null if the attribute
     * is not set in the current theme.
     */
    fun Context.getColorStateListOrNull(@AttrRes colorAttributeResId: Int): ColorStateList? {
        val typedValue = AttributesUtils.resolve(this, colorAttributeResId)
        return if (typedValue == null) {
            null
        } else if (typedValue.resourceId != 0) {
            ContextCompat.getColorStateList(this, typedValue.resourceId)
        } else if (typedValue.data != 0) {
            ColorStateList.valueOf(typedValue.data)
        } else {
            null
        }
    }

    private fun Context.resolveColor(typedValue: TypedValue): Int {
        return if (typedValue.resourceId != 0) {
            // Color State List
            getColor(typedValue.resourceId)
        } else {
            // Color Int
            typedValue.data
        }
    }

    private fun Context.resolveColorStateList(typedValue: TypedValue): ColorStateList? {
        return if (typedValue.resourceId != 0) {
            ContextCompat.getColorStateList(this, typedValue.resourceId)
        } else {
            ColorStateList.valueOf(typedValue.data)
        }
    }

    /**
     * Convenience method that calculates [MaterialColors.layer] without
     * an `overlayAlpha` value by passing in `1f` for the alpha value.
     */
    @ColorInt
    fun View.layer(
        @AttrRes backgroundColorAttributeResId: Int,
        @AttrRes overlayColorAttributeResId: Int
    ): Int {
        return layer(backgroundColorAttributeResId, overlayColorAttributeResId, 1f)
    }

    /**
     * Convenience method that wraps [MaterialColors.layer] for layering colors
     * from theme attributes.
     */
    @ColorInt
    fun View.layer(
        @AttrRes backgroundColorAttributeResId: Int,
        @AttrRes overlayColorAttributeResId: Int,
        @FloatRange(from = 0.0, to = 1.0) overlayAlpha: Float
    ): Int {
        val backgroundColor = getColor(backgroundColorAttributeResId)
        val overlayColor = getColor(overlayColorAttributeResId)
        return MaterialColors.layer(backgroundColor, overlayColor, overlayAlpha)
    }

    /**
     * Calculates a color that represents the layering of the `overlayColor` (with `overlayAlpha` applied) on top of the `backgroundColor`.
     */
    @ColorInt
    fun layer(
        @ColorInt backgroundColor: Int,
        @ColorInt overlayColor: Int,
        @FloatRange(from = 0.0, to = 1.0) overlayAlpha: Float
    ): Int {
        val computedAlpha = (Color.alpha(overlayColor) * overlayAlpha).roundToInt()
        val computedOverlayColor = ColorUtils.setAlphaComponent(overlayColor, computedAlpha)
        return layer(backgroundColor, computedOverlayColor)
    }

    /**
     * Calculates a color that represents the layering of the `overlayColor` on top of the
     * `backgroundColor`.
     */
    @ColorInt
    fun layer(@ColorInt backgroundColor: Int, @ColorInt overlayColor: Int): Int {
        return ColorUtils.compositeColors(overlayColor, backgroundColor)
    }

    /**
     * Calculates a new color by multiplying an additional alpha int value to the alpha channel of a
     * color in integer type.
     *
     * @param originalARGB The original color.
     * @param alpha        The additional alpha [0-255].
     * @return The blended color.
     */
    @ColorInt
    fun compositeARGBWithAlpha(@ColorInt originalARGB: Int, @IntRange(from = 0, to = 255) alpha: Int): Int {
        var alpha = alpha
        alpha = Color.alpha(originalARGB) * alpha / 255
        return ColorUtils.setAlphaComponent(originalARGB, alpha)
    }

    /**
     * Determines if a color should be considered light or dark.
     */
    fun isColorLight(@ColorInt color: Int): Boolean {
        return color != Color.TRANSPARENT && ColorUtils.calculateLuminance(color) > 0.5
    }

    /**
     * Returns the color int of the given color harmonized with the context theme's colorPrimary.
     *
     * @param colorToHarmonize The color to harmonize.
     */
    @ColorInt
    fun Context.harmonizeWithPrimary(@ColorInt colorToHarmonize: Int): Int {
        return harmonize(
            colorToHarmonize,
            getColor(R.attr.colorPrimary, MaterialColors::class.java.getCanonicalName())
        )
    }

    /**
     * A convenience function to harmonize any two colors provided, returns the color int of the
     * harmonized color, or the original design color value if color harmonization is not available.
     *
     * @param colorToHarmonize     The color to harmonize.
     * @param colorToHarmonizeWith The primary color selected for harmonization.
     */
    @ColorInt
    fun harmonize(@ColorInt colorToHarmonize: Int, @ColorInt colorToHarmonizeWith: Int): Int {
        return Blend.harmonize(colorToHarmonize, colorToHarmonizeWith)
    }

    /**
     * Returns the [ColorRoles] object generated from the provided input color.
     *
     * @param color   The input color provided for generating its associated four color roles.
     */
    fun Context.getColorRoles(@ColorInt color: Int): ColorRoles {
        return getColorRoles(color, isLightTheme())
    }

    /**
     * Returns the [ColorRoles] object generated from the provided input color.
     *
     * @param color        The input color provided for generating its associated four color roles.
     * @param isLightTheme Whether the input is light themed or not, true if light theme is enabled.
     */
    fun getColorRoles(@ColorInt color: Int, isLightTheme: Boolean): ColorRoles {
        return if (isLightTheme) {
            ColorRoles(
                getColorRole(color, TONE_ACCENT_LIGHT),
                getColorRole(color, TONE_ON_ACCENT_LIGHT),
                getColorRole(color, TONE_ACCENT_CONTAINER_LIGHT),
                getColorRole(color, TONE_ON_ACCENT_CONTAINER_LIGHT)
            )
        } else {
            ColorRoles(
                getColorRole(color, TONE_ACCENT_DARK),
                getColorRole(color, TONE_ON_ACCENT_DARK),
                getColorRole(color, TONE_ACCENT_CONTAINER_DARK),
                getColorRole(color, TONE_ON_ACCENT_CONTAINER_DARK)
            )
        }
    }

    /**
     * Returns the color int of the surface container color role, based on the provided input color.
     * This method should be only used internally.
     *
     * @param seedColor The input color provided for generating surface container color role.
     * @hide
     */
    @ColorInt
    internal fun Context.getSurfaceContainerFromSeed(@ColorInt seedColor: Int): Int {
        val tone = if (isLightTheme()) TONE_SURFACE_CONTAINER_LIGHT else TONE_SURFACE_CONTAINER_DARK
        return getColorRole(seedColor, tone, CHROMA_NEUTRAL)
    }

    /**
     * Returns the color int of the surface container high color role, based on the provided input
     * color. This method should be only used internally.
     *
     * @param seedColor The input color provided for generating surface container high color role.
     * @hide
     */
    @ColorInt
    internal fun Context.getSurfaceContainerHighFromSeed(@ColorInt seedColor: Int): Int {
        val tone =
            if (isLightTheme()) {
                TONE_SURFACE_CONTAINER_HIGH_LIGHT
            } else {
                TONE_SURFACE_CONTAINER_HIGH_DARK
            }
        return getColorRole(seedColor, tone, CHROMA_NEUTRAL)
    }

    fun Context.isLightTheme(): Boolean {
        return AttributesUtils.resolveBoolean(this, R.attr.isLightTheme, true)
    }

    @ColorInt
    private fun getColorRole(
        @ColorInt color: Int,
        @IntRange(from = 0, to = 100) tone: Int
    ): Int {
        return Hct(color).copy(tone = tone.toDouble()).asArgb()
    }

    @ColorInt
    private fun getColorRole(
        @ColorInt color: Int,
        @IntRange(from = 0, to = 100) tone: Int,
        chroma: Int
    ): Int {
        return Hct(color).copy(chroma = chroma.toDouble(), tone = tone.toDouble()).asArgb()
    }
}
