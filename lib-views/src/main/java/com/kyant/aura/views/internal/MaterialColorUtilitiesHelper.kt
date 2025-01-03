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
package com.kyant.aura.views.internal

import android.util.SparseIntArray
import com.google.android.material.R
import com.kyant.aura.core.dynamiccolor.DynamicColor
import com.kyant.aura.core.dynamiccolor.DynamicScheme
import com.kyant.aura.core.dynamiccolor.MaterialDynamicColors

/**
 * Helper methods for communication with the Material Color Utilities library.
 */
internal object MaterialColorUtilitiesHelper {
    private val dynamicColors = MaterialDynamicColors()

    @Suppress("PrivateResource")
    private val colorResourceIdToColorValue: Map<Int, DynamicColor> =
        HashMap<Int, DynamicColor>().apply {
            put(R.color.material_personalized_color_primary, dynamicColors.primary())
            put(R.color.material_personalized_color_on_primary, dynamicColors.onPrimary())
            put(R.color.material_personalized_color_primary_inverse, dynamicColors.inversePrimary())
            put(R.color.material_personalized_color_primary_container, dynamicColors.primaryContainer())
            put(R.color.material_personalized_color_on_primary_container, dynamicColors.onPrimaryContainer())
            put(R.color.material_personalized_color_secondary, dynamicColors.secondary())
            put(R.color.material_personalized_color_on_secondary, dynamicColors.onSecondary())
            put(R.color.material_personalized_color_secondary_container, dynamicColors.secondaryContainer())
            put(R.color.material_personalized_color_on_secondary_container, dynamicColors.onSecondaryContainer())
            put(R.color.material_personalized_color_tertiary, dynamicColors.tertiary())
            put(R.color.material_personalized_color_on_tertiary, dynamicColors.onTertiary())
            put(R.color.material_personalized_color_tertiary_container, dynamicColors.tertiaryContainer())
            put(R.color.material_personalized_color_on_tertiary_container, dynamicColors.onTertiaryContainer())
            put(R.color.material_personalized_color_background, dynamicColors.background())
            put(R.color.material_personalized_color_on_background, dynamicColors.onBackground())
            put(R.color.material_personalized_color_surface, dynamicColors.surface())
            put(R.color.material_personalized_color_on_surface, dynamicColors.onSurface())
            put(R.color.material_personalized_color_surface_variant, dynamicColors.surfaceVariant())
            put(R.color.material_personalized_color_on_surface_variant, dynamicColors.onSurfaceVariant())
            put(R.color.material_personalized_color_surface_inverse, dynamicColors.inverseSurface())
            put(R.color.material_personalized_color_on_surface_inverse, dynamicColors.inverseOnSurface())
            put(R.color.material_personalized_color_surface_bright, dynamicColors.surfaceBright())
            put(R.color.material_personalized_color_surface_dim, dynamicColors.surfaceDim())
            put(R.color.material_personalized_color_surface_container, dynamicColors.surfaceContainer())
            put(R.color.material_personalized_color_surface_container_low, dynamicColors.surfaceContainerLow())
            put(R.color.material_personalized_color_surface_container_high, dynamicColors.surfaceContainerHigh())
            put(R.color.material_personalized_color_surface_container_lowest, dynamicColors.surfaceContainerLowest())
            put(R.color.material_personalized_color_surface_container_highest, dynamicColors.surfaceContainerHighest())
            put(R.color.material_personalized_color_outline, dynamicColors.outline())
            put(R.color.material_personalized_color_outline_variant, dynamicColors.outlineVariant())
            put(R.color.material_personalized_color_error, dynamicColors.error())
            put(R.color.material_personalized_color_on_error, dynamicColors.onError())
            put(R.color.material_personalized_color_error_container, dynamicColors.errorContainer())
            put(R.color.material_personalized_color_on_error_container, dynamicColors.onErrorContainer())
            put(R.color.material_personalized_color_control_activated, dynamicColors.controlActivated())
            put(R.color.material_personalized_color_control_normal, dynamicColors.controlNormal())
            put(R.color.material_personalized_color_control_highlight, dynamicColors.controlHighlight())
            put(R.color.material_personalized_color_text_primary_inverse, dynamicColors.textPrimaryInverse())
            put(
                R.color.material_personalized_color_text_secondary_and_tertiary_inverse,
                dynamicColors.textSecondaryAndTertiaryInverse()
            )
            put(
                R.color.material_personalized_color_text_secondary_and_tertiary_inverse_disabled,
                dynamicColors.textSecondaryAndTertiaryInverseDisabled()
            )
            put(
                R.color.material_personalized_color_text_primary_inverse_disable_only,
                dynamicColors.textPrimaryInverseDisableOnly()
            )
            put(R.color.material_personalized_color_text_hint_foreground_inverse, dynamicColors.textHintInverse())
        }

    fun createColorResourcesIdsToColorValues(colorScheme: DynamicScheme): SparseIntArray {
        val map = SparseIntArray(colorResourceIdToColorValue.size)
        for (entry in colorResourceIdToColorValue.entries) {
            map.put(entry.key, entry.value.getArgb(colorScheme))
        }
        return map
    }
}
