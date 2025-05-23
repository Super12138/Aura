/*
 * Copyright 2022 Google LLC
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
package com.kyant.aura.core.dislike

import com.kyant.aura.core.hct.Hct

/**
 * Check and/or fix universally disliked colors.
 *
 *
 * Color science studies of color preference indicate universal distaste for dark yellow-greens,
 * and also show this is correlated to distate for biological waste and rotting food.
 *
 *
 * See Palmer and Schloss, 2010 or Schloss and Palmer's Chapter 21 in Handbook of Color
 * Psychology (2015).
 */
object DislikeAnalyzer {
    /**
     * Returns true if color is disliked.
     *
     *
     * Disliked is defined as a dark yellow-green that is not neutral.
     */
    @Suppress("NOTHING_TO_INLINE")
    @JvmStatic
    inline fun Hct.isDisliked(): Boolean {
        return hue >= 89.5 && hue <= 111.5 && chroma > 16.5 && tone < 64.5
    }

    /**
     * If color is disliked, lighten it to make it likable.
     */
    @Suppress("NOTHING_TO_INLINE")
    @JvmStatic
    inline fun Hct.fixIfDisliked(): Hct {
        return if (isDisliked()) {
            Hct(hue, chroma, 70.0)
        } else {
            this
        }
    }
}
