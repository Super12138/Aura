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
package com.kyant.aura.views.internal

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes

/**
 * Utility methods to work with attributes.
 */
internal object AttributesUtils {
    /**
     * Returns the [TypedValue] for the provided `attributeResId` or null if the attribute
     * is not present in the current theme.
     */
    fun resolve(context: Context, @AttrRes attributeResId: Int): TypedValue? {
        val typedValue = TypedValue()
        if (context.theme.resolveAttribute(attributeResId, typedValue, true)) {
            return typedValue
        }
        return null
    }

    fun resolveTypedValueOrThrow(componentView: View, @AttrRes attributeResId: Int): TypedValue {
        return resolveTypedValueOrThrow(
            componentView.context,
            attributeResId,
            componentView.javaClass.getCanonicalName()
        )
    }

    fun resolveTypedValueOrThrow(
        context: Context,
        @AttrRes attributeResId: Int,
        errorMessageComponent: String?
    ): TypedValue {
        val typedValue = resolve(context, attributeResId)
        if (typedValue == null) {
            throw IllegalArgumentException(
                "$errorMessageComponent requires a value for the ${context.resources.getResourceName(attributeResId)} " +
                        "attribute to be set in your app theme. "
                        + "You can either set the attribute in your theme or "
                        + "update your theme to inherit from Theme.MaterialComponents (or a descendant)."
            )
        }
        return typedValue
    }

    /**
     * Returns the boolean value for the provided `attributeResId` or `defaultValue` if
     * the attribute is not a boolean or not present in the current theme.
     */
    fun resolveBoolean(context: Context, @AttrRes attributeResId: Int, defaultValue: Boolean): Boolean {
        val typedValue = resolve(context, attributeResId)
        return if (typedValue != null && typedValue.type == TypedValue.TYPE_INT_BOOLEAN) {
            typedValue.data != 0
        } else {
            defaultValue
        }
    }
}
