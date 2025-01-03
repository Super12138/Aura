package com.kyant.aura.views.dynamic

import android.graphics.Bitmap
import androidx.annotation.ColorInt
import com.kyant.aura.core.quantize.QuantizerCelebi
import com.kyant.aura.core.score.Score

sealed interface DynamicColorSource {
    @get:ColorInt
    val seedColor: Int
}

class ContentBasedDynamicColorSource(
    /** The source color to generate Material color palette. */
    @ColorInt override val seedColor: Int
) : DynamicColorSource

class UserGeneratedDynamicColorSource(
    /** The source image from which to extract the seed color for the Material color palette. */
    val bitmap: Bitmap
) : DynamicColorSource {
    @get:ColorInt
    override val seedColor: Int
        get() {
            val width = bitmap.width
            val height = bitmap.height
            val bitmapPixels = IntArray(width * height)
            bitmap.getPixels(bitmapPixels, 0, width, 0, 0, width, height)
            return Score.score(QuantizerCelebi.quantize(bitmapPixels, 128)).first()
        }
}
