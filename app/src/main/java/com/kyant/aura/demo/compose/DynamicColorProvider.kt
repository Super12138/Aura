package com.kyant.aura.demo.compose

import com.kyant.aura.core.dynamiccolor.DynamicScheme
import com.kyant.aura.core.palettes.TonalPalette

class DynamicColorProvider(scheme: DynamicScheme) : ColorProvider() {
    override val neutral1: IntArray = scheme.neutralPalette.shades

    override val neutral2: IntArray = scheme.neutralVariantPalette.shades

    override val accent1: IntArray = scheme.primaryPalette.shades

    override val accent2: IntArray = scheme.secondaryPalette.shades

    override val accent3: IntArray = scheme.tertiaryPalette.shades

    private val TonalPalette.shades: IntArray
        get() = intArrayOf(
            tone(100),
            tone(99),
            tone(95),
            tone(90),
            tone(80),
            tone(70),
            tone(60),
            tone(50),
            tone(40),
            tone(30),
            tone(20),
            tone(10),
            tone(0)
        )
}
