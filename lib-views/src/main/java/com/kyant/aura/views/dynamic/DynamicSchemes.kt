package com.kyant.aura.views.dynamic

import com.kyant.aura.core.dynamiccolor.DynamicScheme
import com.kyant.aura.core.dynamiccolor.Variant
import com.kyant.aura.core.hct.Hct
import com.kyant.aura.core.scheme.SchemeContent
import com.kyant.aura.core.scheme.SchemeExpressive
import com.kyant.aura.core.scheme.SchemeFidelity
import com.kyant.aura.core.scheme.SchemeFruitSalad
import com.kyant.aura.core.scheme.SchemeMonochrome
import com.kyant.aura.core.scheme.SchemeNeutral
import com.kyant.aura.core.scheme.SchemeRainbow
import com.kyant.aura.core.scheme.SchemeTonalSpot
import com.kyant.aura.core.scheme.SchemeVibrant

object DynamicSchemes {
    fun createDynamicScheme(
        sourceColorHct: Hct,
        variant: Variant,
        isDark: Boolean,
        contrastLevel: Double
    ): DynamicScheme {
        return when (variant) {
            Variant.MONOCHROME -> SchemeMonochrome(sourceColorHct, isDark, contrastLevel)
            Variant.NEUTRAL -> SchemeNeutral(sourceColorHct, isDark, contrastLevel)
            Variant.TONAL_SPOT -> SchemeTonalSpot(sourceColorHct, isDark, contrastLevel)
            Variant.VIBRANT -> SchemeVibrant(sourceColorHct, isDark, contrastLevel)
            Variant.EXPRESSIVE -> SchemeExpressive(sourceColorHct, isDark, contrastLevel)
            Variant.FIDELITY -> SchemeFidelity(sourceColorHct, isDark, contrastLevel)
            Variant.CONTENT -> SchemeContent(sourceColorHct, isDark, contrastLevel)
            Variant.RAINBOW -> SchemeRainbow(sourceColorHct, isDark, contrastLevel)
            Variant.FRUIT_SALAD -> SchemeFruitSalad(sourceColorHct, isDark, contrastLevel)
        }
    }
}
