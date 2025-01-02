package com.kyant.m3color.hct

import com.kyant.m3color.blend.Blend
import com.kyant.m3color.dynamiccolor.MaterialDynamicColors
import com.kyant.m3color.scheme.SchemeContent
import com.kyant.m3color.scheme.SchemeExpressive
import com.kyant.m3color.scheme.SchemeFidelity
import com.kyant.m3color.scheme.SchemeFruitSalad
import com.kyant.m3color.scheme.SchemeNeutral
import com.kyant.m3color.scheme.SchemeRainbow
import com.kyant.m3color.scheme.SchemeTonalSpot
import com.kyant.m3color.scheme.SchemeVibrant
import kotlin.test.Test
import kotlin.test.assertEquals

class TruthTests {
    @Test
    fun hct() {
        run {
            val c = Hct.fromInt(0x6200EE).run {
                Hct.from(hue, chroma, tone)
            }
            val c2 = com.kyant.aura.core.hct.Hct(0x6200EE).run {
                com.kyant.aura.core.hct.Hct(hue, chroma, tone)
            }
            assertEquals(c, c2)
        }

        run {
            val c = Hct.fromInt(0x03DAC6).run {
                Hct.from(hue, chroma, tone)
            }
            val c2 = com.kyant.aura.core.hct.Hct(0x03DAC6).run {
                com.kyant.aura.core.hct.Hct(hue, chroma, tone)
            }
            assertEquals(c, c2)
        }

        run {
            val c = Hct.fromInt(0xFFFFFF).run {
                Hct.from(hue, chroma, tone)
            }
            val c2 = com.kyant.aura.core.hct.Hct(0xFFFFFF).run {
                com.kyant.aura.core.hct.Hct(hue, chroma, tone)
            }
            assertEquals(c, c2)
        }

        run {
            val c = Hct.fromInt(0x000000).run {
                Hct.from(hue, chroma, tone)
            }
            val c2 = com.kyant.aura.core.hct.Hct(0x000000).run {
                com.kyant.aura.core.hct.Hct(hue, chroma, tone)
            }
            assertEquals(c, c2)
        }

        run {
            val c = Hct.from(270.0, 100.0, 40.0)
            val c2 = com.kyant.aura.core.hct.Hct(270.0, 100.0, 40.0)
            assertEquals(c, c2)
        }

        run {
            val c = Hct.from(270.0, 0.0, 40.0)
            val c2 = com.kyant.aura.core.hct.Hct(270.0, 0.0, 40.0)
            assertEquals(c, c2)
        }

        run {
            val c = Hct.from(0.0, 0.0, 40.0)
            val c2 = com.kyant.aura.core.hct.Hct(0.0, 0.0, 40.0)
            assertEquals(c, c2)
        }

        run {
            val c = Hct.from(0.0, 10.0, 99.99)
            val c2 = com.kyant.aura.core.hct.Hct(0.0, 10.0, 99.99)
            assertEquals(c, c2)
        }

        run {
            val c = Hct.from(0.0, 10000.0, 40.0)
            val c2 = com.kyant.aura.core.hct.Hct(0.0, 10000.0, 40.0)
            assertEquals(c, c2)
        }

        run {
            val c = Hct.from(0.0, 10.0, 10000.0)
            val c2 = com.kyant.aura.core.hct.Hct(0.0, 10.0, 10000.0)
            assertEquals(c, c2)
        }

        run {
            val c = Hct.from(1000.0, 10000.0, -10000.0)
            val c2 = com.kyant.aura.core.hct.Hct(1000.0, 10000.0, -10000.0)
            assertEquals(c, c2)
        }
    }

    @Test
    fun blend() {
        run {
            val c = Blend.harmonize(0x6200EE, 0x03DAC6)
            val c2 = com.kyant.aura.core.blend.Blend.harmonize(0x6200EE, 0x03DAC6)
            assertEquals(c, c2)
        }

        run {
            val c = Blend.harmonize(0x03DAC6, 0x6200EE)
            val c2 = com.kyant.aura.core.blend.Blend.harmonize(0x03DAC6, 0x6200EE)
            assertEquals(c, c2)
        }

        run {
            val c = Blend.harmonize(0xFFFFFF, 0x000000)
            val c2 = com.kyant.aura.core.blend.Blend.harmonize(0xFFFFFF, 0x000000)
            assertEquals(c, c2)
        }

        run {
            val c = Blend.harmonize(0x000000, 0xFFFFFF)
            val c2 = com.kyant.aura.core.blend.Blend.harmonize(0x000000, 0xFFFFFF)
            assertEquals(c, c2)
        }

        run {
            val c = Blend.hctHue(0x6200EE, 0x03DAC6, 0.5)
            val c2 = com.kyant.aura.core.blend.Blend.hctHue(0x6200EE, 0x03DAC6, 0.5)
            assertEquals(c, c2)
        }

        run {
            val c = Blend.hctHue(0x03DAC6, 0x6200EE, 0.5)
            val c2 = com.kyant.aura.core.blend.Blend.hctHue(0x03DAC6, 0x6200EE, 0.5)
            assertEquals(c, c2)
        }

        run {
            val c = Blend.hctHue(0xFFFFFF, 0x000000, 0.5)
            val c2 = com.kyant.aura.core.blend.Blend.hctHue(0xFFFFFF, 0x000000, 0.5)
            assertEquals(c, c2)
        }
    }

    @Test
    fun solveHct() {
        val chroma = 200.0
        val hues = (0..<360).map { it.toDouble() }
        val tones = (0..100).map { it.toDouble() }
        hues.forEach { hue ->
            tones.forEach { tone ->
                assertEquals(
                    HctSolver.solveToInt(hue, chroma, tone),
                    com.kyant.aura.core.hct.HctSolver.solveToInt(hue, chroma, tone)
                )
            }
        }
    }

    @Test
    fun palette() {
        val contrasts = listOf(-1.0, -0.5, 0.0, 0.5, 1.0)
        val hues = (0..<360 step 30).toList()
        val chroma = 200.0
        val tone = 40.0
        val colors = ArrayList<Pair<Hct, com.kyant.aura.core.hct.Hct>>(hues.size)
        for (i in hues.indices) {
            val hue = hues[i].toDouble()
            colors += Hct.from(hue, chroma, tone) to com.kyant.aura.core.hct.Hct(hue, chroma, tone)
        }

        for (isDark in listOf(true, false)) {
            for (contrast in contrasts) {
                for ((hct, hct2) in colors) {
                    apply {
                        val scheme1 = SchemeNeutral(hct, isDark, contrast)
                        val scheme2 = com.kyant.aura.core.scheme.SchemeNeutral(hct2, isDark, contrast)

                        val mdc1 = MaterialDynamicColors()
                        val mdc2 = com.kyant.aura.core.dynamiccolor.MaterialDynamicColors()

                        mdc1.allDynamicColors().zip(mdc2.allDynamicColors()).forEach {
                            assertEquals(scheme1.getHct(it.first.get()), scheme2.getHct(it.second.invoke()))
                        }
                    }
                    apply {
                        val scheme1 = SchemeTonalSpot(hct, isDark, contrast)
                        val scheme2 = com.kyant.aura.core.scheme.SchemeTonalSpot(hct2, isDark, contrast)

                        val mdc1 = MaterialDynamicColors()
                        val mdc2 = com.kyant.aura.core.dynamiccolor.MaterialDynamicColors()

                        mdc1.allDynamicColors().zip(mdc2.allDynamicColors()).forEach {
                            assertEquals(scheme1.getHct(it.first.get()), scheme2.getHct(it.second.invoke()))
                        }
                    }
                    apply {
                        val scheme1 = SchemeVibrant(hct, isDark, contrast)
                        val scheme2 = com.kyant.aura.core.scheme.SchemeVibrant(hct2, isDark, contrast)

                        val mdc1 = MaterialDynamicColors()
                        val mdc2 = com.kyant.aura.core.dynamiccolor.MaterialDynamicColors()

                        mdc1.allDynamicColors().zip(mdc2.allDynamicColors()).forEach {
                            assertEquals(scheme1.getHct(it.first.get()), scheme2.getHct(it.second.invoke()))
                        }
                    }
                    apply {
                        val scheme1 = SchemeExpressive(hct, isDark, contrast)
                        val scheme2 = com.kyant.aura.core.scheme.SchemeExpressive(hct2, isDark, contrast)

                        val mdc1 = MaterialDynamicColors()
                        val mdc2 = com.kyant.aura.core.dynamiccolor.MaterialDynamicColors()

                        mdc1.allDynamicColors().zip(mdc2.allDynamicColors()).forEach {
                            assertEquals(scheme1.getHct(it.first.get()), scheme2.getHct(it.second.invoke()))
                        }
                    }
                    apply {
                        val scheme1 = SchemeFidelity(hct, isDark, contrast)
                        val scheme2 = com.kyant.aura.core.scheme.SchemeFidelity(hct2, isDark, contrast)

                        val mdc1 = MaterialDynamicColors()
                        val mdc2 = com.kyant.aura.core.dynamiccolor.MaterialDynamicColors()

                        mdc1.allDynamicColors().zip(mdc2.allDynamicColors()).forEach {
                            assertEquals(scheme1.getHct(it.first.get()), scheme2.getHct(it.second.invoke()))
                        }
                    }
                    apply {
                        val scheme1 = SchemeContent(hct, isDark, contrast)
                        val scheme2 = com.kyant.aura.core.scheme.SchemeContent(hct2, isDark, contrast)

                        val mdc1 = MaterialDynamicColors()
                        val mdc2 = com.kyant.aura.core.dynamiccolor.MaterialDynamicColors()

                        mdc1.allDynamicColors().zip(mdc2.allDynamicColors()).forEach {
                            assertEquals(scheme1.getHct(it.first.get()), scheme2.getHct(it.second.invoke()))
                        }
                    }
                    apply {
                        val scheme1 = SchemeRainbow(hct, isDark, contrast)
                        val scheme2 = com.kyant.aura.core.scheme.SchemeRainbow(hct2, isDark, contrast)

                        val mdc1 = MaterialDynamicColors()
                        val mdc2 = com.kyant.aura.core.dynamiccolor.MaterialDynamicColors()

                        mdc1.allDynamicColors().zip(mdc2.allDynamicColors()).forEach {
                            assertEquals(scheme1.getHct(it.first.get()), scheme2.getHct(it.second.invoke()))
                        }
                    }
                    apply {
                        val scheme1 = SchemeFruitSalad(hct, isDark, contrast)
                        val scheme2 = com.kyant.aura.core.scheme.SchemeFruitSalad(hct2, isDark, contrast)

                        val mdc1 = MaterialDynamicColors()
                        val mdc2 = com.kyant.aura.core.dynamiccolor.MaterialDynamicColors()

                        mdc1.allDynamicColors().zip(mdc2.allDynamicColors()).forEach {
                            assertEquals(scheme1.getHct(it.first.get()), scheme2.getHct(it.second.invoke()))
                        }
                    }
                }
            }
        }
    }

    private fun assertEquals(a: Hct, b: com.kyant.aura.core.hct.Hct) {
        tryAssertEquals(a.hue, b.hue)
        tryAssertEquals(a.chroma, b.chroma)
        tryAssertEquals(a.tone, b.tone)
        assertEquals(a.toInt(), b.asArgb())
    }

    private fun tryAssertEquals(a: Double, b: Double) {
        try {
            assertEquals(a, b)
        } catch (e: AssertionError) {
            // println(e.message)
            assertEquals(a, b, 1E-10)
        }
    }
}
