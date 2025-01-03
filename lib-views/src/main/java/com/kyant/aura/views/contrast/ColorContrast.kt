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

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.app.UiModeManager
import android.app.UiModeManager.ContrastChangeListener
import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import com.kyant.aura.views.internal.ThemeUtils

/**
 * Utility for applying contrast colors to application/activities.
 *
 *
 * Please note that if you are already using dynamic colors, contrast will be applied
 * automatically on Android U+. This is only needed if you have a branded or custom theme and want
 * to support contrast.
 */
object ColorContrast {
    private const val MEDIUM_CONTRAST_THRESHOLD = 1 / 3f
    private const val HIGH_CONTRAST_THRESHOLD = 2 / 3f

    /**
     * Applies contrast to all activities by registering a [ActivityLifecycleCallbacks] to your
     * application.
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
     * ColorContrast.applyToActivitiesIfAvailable(this);
     * }
     * }
    </pre> *
     *
     *
     * This method will try to apply a theme overlay in every activity's [ ][ActivityLifecycleCallbacks.onActivityPreCreated] callback.
     *
     * @param application          The target application.
     * @param colorContrastOptions The color contrast options object that specifies the theme overlay
     * resource IDs for medium and high contrast mode.
     */
    @JvmStatic
    @JvmOverloads
    fun applyToActivitiesIfAvailable(
        application: Application,
        colorContrastOptions: ColorContrastOptions = ColorContrastOptions.Companion.Default
    ) {
        if (!isContrastAvailable) {
            return
        }

        application.registerActivityLifecycleCallbacks(ColorContrastActivityLifecycleCallbacks(colorContrastOptions))
    }

    /**
     * Applies contrast to the given activity.
     *
     *
     * Note that this method does not guarantee the consistency of contrast throughout the app. If
     * you want contrast to be updated automatically when a different contrast level is selected in
     * the system, please use #applyToActivitiesIfAvailable(Application, ColorContrastOptions).
     *
     * @param activity             The target activity.
     * @param colorContrastOptions The color contrast options object that specifies the theme overlay
     * resource IDs for medium and high contrast mode.
     */
    @JvmStatic
    @JvmOverloads
    fun applyToActivityIfAvailable(
        activity: Activity,
        colorContrastOptions: ColorContrastOptions = ColorContrastOptions.Companion.Default
    ) {
        if (!isContrastAvailable) {
            return
        }

        val themeOverlayResourcesId = getContrastThemeOverlayResourceId(activity, colorContrastOptions)
        if (themeOverlayResourcesId != 0) {
            ThemeUtils.applyThemeOverlay(activity, themeOverlayResourcesId)
        }
    }

    /**
     * Wraps the given context with the theme overlay where color resources are updated. The returned
     * context can be used to create views with contrast support.
     *
     *
     * Note that this method does not guarantee the consistency of contrast throughout the app. If
     * you want contrast to be updated automatically when a different contrast level is selected in
     * the system, please use #applyToActivitiesIfAvailable(Application, ColorContrastOptions).
     *
     * @param context              The target context.
     * @param colorContrastOptions The color contrast options object that specifies the theme overlay
     * resource IDs for medium and high contrast mode.
     */
    @JvmStatic
    @JvmOverloads
    fun wrapContextIfAvailable(
        context: Context,
        colorContrastOptions: ColorContrastOptions = ColorContrastOptions.Companion.Default
    ): Context {
        if (!isContrastAvailable) {
            return context
        }

        val themeOverlayResourcesId = getContrastThemeOverlayResourceId(context, colorContrastOptions)
        if (themeOverlayResourcesId == 0) {
            return context
        }
        return ContextThemeWrapper(context, themeOverlayResourcesId)
    }

    @get:ChecksSdkIntAtLeast(api = VERSION_CODES.UPSIDE_DOWN_CAKE)
    val isContrastAvailable: Boolean
        /**
         * Returns `true` if contrast control is available on the current SDK level.
         */
        get() = Build.VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE

    private fun getContrastThemeOverlayResourceId(context: Context, colorContrastOptions: ColorContrastOptions): Int {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager?
        if (!isContrastAvailable || uiModeManager == null) {
            return 0
        }

        val currentContrast = uiModeManager.contrast
        val mediumContrastThemeOverlay = colorContrastOptions.mediumContrastThemeOverlay
        val highContrastThemeOverlay = colorContrastOptions.highContrastThemeOverlay
        if (currentContrast >= HIGH_CONTRAST_THRESHOLD) {
            // Falls back to mediumContrastThemeOverlay if highContrastThemeOverlay is not set in
            // ColorContrastOptions. If mediumContrastThemeOverlay is not set, default 0 will be returned.
            return if (highContrastThemeOverlay == 0) {
                mediumContrastThemeOverlay
            } else {
                highContrastThemeOverlay
            }
        } else if (currentContrast >= MEDIUM_CONTRAST_THRESHOLD) {
            // Falls back to highContrastThemeOverlay if mediumContrastThemeOverlay is not set in
            // ColorContrastOptions. If highContrastThemeOverlay is not set, default 0 will be returned.
            return if (mediumContrastThemeOverlay == 0) {
                highContrastThemeOverlay
            } else {
                mediumContrastThemeOverlay
            }
        }
        return 0
    }

    @RequiresApi(VERSION_CODES.UPSIDE_DOWN_CAKE)
    private class ColorContrastActivityLifecycleCallbacks(
        private val colorContrastOptions: ColorContrastOptions
    ) : ActivityLifecycleCallbacks {
        private val activitiesInStack: MutableSet<Activity> = LinkedHashSet<Activity>()

        private var contrastChangeListener: ContrastChangeListener? = null

        override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
            val uiModeManager = activity.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager?
            if (uiModeManager != null && activitiesInStack.isEmpty() && contrastChangeListener == null) {
                val contrastChangeListener =
                    object : ContrastChangeListener {
                        override fun onContrastChanged(contrastLevel: Float) {
                            for (activityInStack in activitiesInStack) {
                                activityInStack.recreate()
                            }
                        }
                    }
                this.contrastChangeListener = contrastChangeListener
                // Register UiContrastChangeListener on the application level.
                uiModeManager.addContrastChangeListener(
                    activity.applicationContext.mainExecutor,
                    contrastChangeListener
                )
            }

            activitiesInStack.add(activity)
            if (uiModeManager != null) {
                applyToActivityIfAvailable(activity, colorContrastOptions)
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            // Always remove the activity from the stack to avoid memory leak.
            activitiesInStack.remove(activity)

            val uiModeManager = activity.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager?
            val contrastChangeListener = contrastChangeListener
            if (uiModeManager != null && contrastChangeListener != null && activitiesInStack.isEmpty()) {
                uiModeManager.removeContrastChangeListener(contrastChangeListener)
                this.contrastChangeListener = null
            }
        }
    }
}
