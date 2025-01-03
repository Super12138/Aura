/*
 * Copyright (C) 2023 The Android Open Source Project
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
package com.kyant.aura.views.contrast

import androidx.annotation.StyleRes

/**
 * Wrapper class for specifying color contrast options when applying contrast to branded and custom
 * themes. Clients have the options to provide theme overlay resource ids for medium and high
 * contrast mode.
 *
 *
 * An example of the provided theme overlay resource ids could be one of the following:
 *
 *
 *  * contrast in light mode: R.style.ThemeOverlay_XxxContrast_Light
 *  * contrast in dark mode: R.style.ThemeOverlay_XxxContrast_Dark
 *  * contrast in both light and dark mode: R.style.ThemeOverlay_XxxContrast_DayNight
 *
 */
class ColorContrastOptions(
    /**
     * The resource id of the medium contrast theme overlay.
     */
    @StyleRes
    val mediumContrastThemeOverlay: Int = 0,
    /**
     * The resource id of the high contrast theme overlay.
     */
    @StyleRes
    val highContrastThemeOverlay: Int = 0
) {
    companion object {
        val Default = ColorContrastOptions()
    }
}
