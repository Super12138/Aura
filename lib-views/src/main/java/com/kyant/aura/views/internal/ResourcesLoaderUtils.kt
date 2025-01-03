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
import android.util.TypedValue
import androidx.annotation.RequiresApi

/**
 * Utility methods for Resources Loader, only support sdks R+.
 */
@RequiresApi(api = Build.VERSION_CODES.R)
internal object ResourcesLoaderUtils {
    fun addResourcesLoaderToContext(context: Context, colorReplacementMap: SparseIntArray): Boolean {
        val resourcesLoader = ColorResourcesLoaderCreator.create(context, colorReplacementMap)
        if (resourcesLoader != null) {
            context.resources.addLoaders(resourcesLoader)
            return true
        }
        return false
    }

    fun isColorResource(attrType: Int): Boolean {
        return TypedValue.TYPE_FIRST_COLOR_INT <= attrType && attrType <= TypedValue.TYPE_LAST_COLOR_INT
    }
}
