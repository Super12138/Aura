/*
 * Copyright (C) 2021 The Android Open Source Project
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
import android.app.Application
import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.annotation.ChecksSdkIntAtLeast
import com.google.android.material.R
import com.kyant.aura.core.hct.Hct
import com.kyant.aura.views.MaterialColors.isLightTheme
import com.kyant.aura.views.internal.ColorResourcesOverride
import com.kyant.aura.views.internal.MaterialColorUtilitiesHelper
import com.kyant.aura.views.internal.ThemeUtils

/** Utility for applying dynamic colors to application/activities. */
object DynamicColors {
    private val DYNAMIC_COLOR_THEME_OVERLAY_ATTRIBUTE = intArrayOf(R.attr.dynamicColorThemeOverlay)

    private val DEFAULT_DEVICE_SUPPORT_CONDITION = DeviceSupportCondition { true }

    @Suppress("DiscouragedPrivateApi")
    private val SAMSUNG_DEVICE_SUPPORT_CONDITION: DeviceSupportCondition = object : DeviceSupportCondition {
        private val version: Long by lazy(LazyThreadSafetyMode.NONE) {
            try {
                val method = Build::class.java.getDeclaredMethod("getLong", String::class.java)
                method.isAccessible = true
                method.invoke(null, "ro.build.version.oneui") as Long
            } catch (_: Exception) {
                -1L
            }
        }

        override fun isSupported(): Boolean {
            return version >= 40100L
        }
    }

    private val DYNAMIC_COLOR_SUPPORTED_MANUFACTURERS: Map<String, DeviceSupportCondition> =
        HashMap<String, DeviceSupportCondition>().apply {
            put("fcnt", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("google", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("hmd global", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("infinix", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("infinix mobility limited", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("itel", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("kyocera", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("lenovo", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("lge", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("meizu", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("motorola", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("nothing", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("oneplus", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("oppo", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("realme", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("robolectric", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("samsung", SAMSUNG_DEVICE_SUPPORT_CONDITION)
            put("sharp", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("shift", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("sony", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("tcl", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("tecno", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("tecno mobile limited", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("vivo", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("wingtech", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("xiaomi", DEFAULT_DEVICE_SUPPORT_CONDITION)
        }
    private val DYNAMIC_COLOR_SUPPORTED_BRANDS: Map<String, DeviceSupportCondition> =
        HashMap<String, DeviceSupportCondition>().apply {
            put("asus", DEFAULT_DEVICE_SUPPORT_CONDITION)
            put("jio", DEFAULT_DEVICE_SUPPORT_CONDITION)
        }

    private const val USE_DEFAULT_THEME_OVERLAY = 0

    /**
     * Applies dynamic colors to all activities based on the provided [DynamicColorsOptions], by
     * registering a [Application.ActivityLifecycleCallbacks] to your application.
     *
     *
     * A normal usage of this method should happen only once in [Application.onCreate] or
     * any methods that run before any of your activities are created. For example:
     *
     * <pre>
     * public class YourApplication extends Application {
     * &#64;Override
     * public void onCreate() {
     * super.onCreate();
     * DynamicColors.applyToActivitiesWithCallbacks(this);
     * }
     * }
    </pre> *
     *
     * This method will try to apply the given dynamic color theme overlay in every activity's [ ][Application.ActivityLifecycleCallbacks.onActivityPreCreated] callback. Therefore, if you
     * are applying any other theme overlays after that, you will need to be careful about not
     * overriding the colors or you may lose the dynamic color support.
     *
     * @param application The target application.
     * @param dynamicColorsOptions The dynamic colors options object that specifies the theme resource
     * ID, precondition to decide if dynamic colors should be applied and the callback function
     * for after dynamic colors have been applied.
     */
    @JvmStatic
    @JvmOverloads
    fun applyToActivitiesIfAvailable(
        application: Application,
        dynamicColorsOptions: DynamicColorsOptions = DynamicColorsOptions.Default
    ) {
        application.registerActivityLifecycleCallbacks(DynamicColorsActivityLifecycleCallbacks(dynamicColorsOptions))
    }

    /**
     * Applies dynamic colors to the given activity with [DynamicColorsOptions] provided.
     *
     * @param activity The target activity.
     * @param dynamicColorsOptions The dynamic colors options object that specifies the theme resource
     * ID, precondition to decide if dynamic colors should be applied and the callback function
     * for after dynamic colors have been applied.
     */
    @JvmStatic
    @JvmOverloads
    fun applyToActivityIfAvailable(
        activity: Activity,
        dynamicColorsOptions: DynamicColorsOptions = DynamicColorsOptions.Default
    ) {
        if (!isDynamicColorAvailable) {
            return
        }
        val theme =
            if (dynamicColorsOptions.seedColor == null) {
                // Only retrieves the theme overlay if we're applying just dynamic colors.
                if (dynamicColorsOptions.themeOverlay == USE_DEFAULT_THEME_OVERLAY) {
                    getDefaultThemeOverlay(activity, DYNAMIC_COLOR_THEME_OVERLAY_ATTRIBUTE)
                } else {
                    dynamicColorsOptions.themeOverlay
                }
            } else {
                // Set default theme overlay as 0, as it's not used in content-based dynamic colors.
                0
            }

        if (!dynamicColorsOptions.precondition.shouldApplyDynamicColors(activity, theme)) {
            return
        }
        // Applies content-based dynamic colors if content-based source is provided.
        if (dynamicColorsOptions.seedColor != null) {
            val resourcesOverride = ColorResourcesOverride.instance
            if (resourcesOverride == null) {
                return
            }
            val contrastLevel =
                if (dynamicColorsOptions.contrastLevel.isNaN()) getSystemContrast(activity).toDouble()
                else dynamicColorsOptions.contrastLevel
            val scheme =
                DynamicSchemes.createDynamicScheme(
                    Hct(dynamicColorsOptions.seedColor),
                    dynamicColorsOptions.variant,
                    !activity.isLightTheme(),
                    contrastLevel
                )
            if (!resourcesOverride.applyIfPossible(
                    activity,
                    MaterialColorUtilitiesHelper.createColorResourcesIdsToColorValues(scheme)
                )
            ) {
                return
            }
        } else {
            ThemeUtils.applyThemeOverlay(activity, theme)
        }
        // Applies client's callback after content-based dynamic colors or just dynamic colors has
        // been applied.
        dynamicColorsOptions.onAppliedCallback.onApplied(activity)
    }

    /**
     * Wraps the given context with the given theme overlay provided in [DynamicColorsOptions].
     * The returned context can be used to create views with dynamic color support.
     *
     *
     * If dynamic color support is not available, the original context will be returned.
     *
     * @param originalContext The original context.
     * @param dynamicColorsOptions The dynamic colors options object that specifies the theme resource
     * ID, seed color for content-based dynamic colors.
     */
    @JvmStatic
    @JvmOverloads
    fun wrapContextIfAvailable(
        originalContext: Context,
        dynamicColorsOptions: DynamicColorsOptions = DynamicColorsOptions.Default
    ): Context {
        if (!isDynamicColorAvailable) {
            return originalContext
        }
        var theme = dynamicColorsOptions.themeOverlay
        if (theme == USE_DEFAULT_THEME_OVERLAY) {
            theme = getDefaultThemeOverlay(originalContext, DYNAMIC_COLOR_THEME_OVERLAY_ATTRIBUTE)
        }

        if (theme == 0) {
            return originalContext
        }

        if (dynamicColorsOptions.seedColor != null) {
            val resourcesOverride = ColorResourcesOverride.instance
            if (resourcesOverride != null) {
                val contrastLevel =
                    if (dynamicColorsOptions.contrastLevel.isNaN()) getSystemContrast(originalContext).toDouble()
                    else dynamicColorsOptions.contrastLevel
                val scheme =
                    DynamicSchemes.createDynamicScheme(
                        Hct(dynamicColorsOptions.seedColor),
                        dynamicColorsOptions.variant,
                        !originalContext.isLightTheme(),
                        contrastLevel
                    )
                return resourcesOverride.wrapContextIfPossible(
                    originalContext,
                    MaterialColorUtilitiesHelper.createColorResourcesIdsToColorValues(scheme)
                )
            }
        }
        return ContextThemeWrapper(originalContext, theme)
    }

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    val isDynamicColorAvailable: Boolean
        /** Returns `true` if dynamic colors are available on the current SDK level.  */
        get() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                return false
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                return true
            }
            val deviceSupportCondition =
                DYNAMIC_COLOR_SUPPORTED_MANUFACTURERS[Build.MANUFACTURER.lowercase()]
                    ?: DYNAMIC_COLOR_SUPPORTED_BRANDS[Build.BRAND.lowercase()]
            return deviceSupportCondition != null && deviceSupportCondition.isSupported()
        }

    private fun getDefaultThemeOverlay(context: Context, themeOverlayAttribute: IntArray): Int {
        val dynamicColorAttributes = context.obtainStyledAttributes(themeOverlayAttribute)
        val theme = dynamicColorAttributes.getResourceId(0, 0)
        dynamicColorAttributes.recycle()
        return theme
    }

    private fun getSystemContrast(context: Context): Float {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager?
        return if (uiModeManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            uiModeManager.contrast
        } else {
            0f
        }
    }

    private class DynamicColorsActivityLifecycleCallbacks(
        private val dynamicColorsOptions: DynamicColorsOptions
    ) : Application.ActivityLifecycleCallbacks {
        override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
            applyToActivityIfAvailable(activity, dynamicColorsOptions)
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {}
    }

    private fun interface DeviceSupportCondition {
        fun isSupported(): Boolean
    }
}
