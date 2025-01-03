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

import android.content.Context
import android.os.Build
import android.util.SparseIntArray

/**
 * The interface class that hides the detailed implementation of color resources override at
 * runtime. (e.g. with Resources Loader implementation pre-U.)
 */
internal interface ColorResourcesOverride {
    /**
     * Overrides the color resources to the given context, returns `true` if new color values
     * have been applied.
     *
     * @param context The target context.
     * @param colorResourceIdsToColorValues The mapping from the color resources id to the updated
     * color value.
     */
    fun applyIfPossible(context: Context, colorResourceIdsToColorValues: SparseIntArray): Boolean

    /**
     * Wraps the given Context with the theme overlay where color resources are updated at runtime. If
     * not possible, the original Context will be returned.
     *
     * @param context The target context.
     * @param colorResourceIdsToColorValues The mapping from the color resources id to the updated
     * color value.
     */
    fun wrapContextIfPossible(context: Context, colorResourceIdsToColorValues: SparseIntArray): Context

    companion object {
        val instance: ColorResourcesOverride?
            get() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    // TODO(b/255833419): Replace with FabricatedOverlayColorResourcesOverride() when available for U+.
                    return ResourcesLoaderColorResourcesOverride
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    return ResourcesLoaderColorResourcesOverride
                }
                return null
            }
    }
}
