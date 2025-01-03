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

import android.app.Activity
import android.content.Context
import androidx.annotation.StyleRes

/**
 * Utility methods for theme.
 */
internal object ThemeUtils {
    fun applyThemeOverlay(context: Context, @StyleRes theme: Int) {
        // Use applyStyle() instead of setTheme() due to Force Dark issue.
        context.theme.applyStyle(theme, true)

        // Make sure the theme overlay is applied to the Window decorView similar to Activity#setTheme,
        // to ensure that it will be applied to things like ContextMenu using the DecorContext.
        if (context is Activity) {
            // Use peekDecorView() instead of getDecorView() to avoid locking the Window.
            context.window?.peekDecorView()?.context?.theme?.applyStyle(theme, true)
        }
    }
}
