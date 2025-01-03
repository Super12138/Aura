package com.kyant.m3color.hct

import com.kyant.m3color.blend.Blend
import com.kyant.m3color.dynamiccolor.MaterialDynamicColors
import com.kyant.m3color.quantize.QuantizerCelebi
import com.kyant.m3color.scheme.SchemeContent
import com.kyant.m3color.scheme.SchemeExpressive
import com.kyant.m3color.scheme.SchemeFidelity
import com.kyant.m3color.scheme.SchemeFruitSalad
import com.kyant.m3color.scheme.SchemeNeutral
import com.kyant.m3color.scheme.SchemeRainbow
import com.kyant.m3color.scheme.SchemeTonalSpot
import com.kyant.m3color.scheme.SchemeVibrant
import com.kyant.m3color.score.Score
import kotlin.test.Test
import kotlin.test.assertContentEquals
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
        val hues = (0..<360 step 1).toList()
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

    @Test
    fun score() {
        val colorsToPopulation = mapOf(
            0xff8b_1d99.toInt() to 54,
            0xff27_effe.toInt() to 43,
            0xff6f_558d.toInt() to 2,
            0xff77_fdf2.toInt() to 78
        )

        val ranked = Score.score(colorsToPopulation, 4, 0, true)
        assertEquals(ranked.size, 3)
        assertEquals(ranked[0], 0xff27_effe.toInt())
        assertEquals(ranked[1], 0xff8b_1d99.toInt())
        assertEquals(ranked[2], 0xff6f_558d.toInt())

        val ranked2 = com.kyant.aura.core.score.Score.score(colorsToPopulation, 4, true)
        assertEquals(ranked2.size, 3)
        assertEquals(ranked2[0], 0xff27_effe.toInt())
        assertEquals(ranked2[1], 0xff8b_1d99.toInt())
        assertEquals(ranked2[2], 0xff6f_558d.toInt())
    }

    @Test
    fun quantize() {
        val red = 0xffff_0000.toInt()
        val green = 0xff00_ff00.toInt()
        val maxColors = 256

        val result = QuantizerCelebi.quantize(intArrayOf(red, red, green, green, green), maxColors)
        assertEquals(result.keys.size, 2)
        assertEquals(result.getValue(green), 3)
        assertEquals(result.getValue(red), 2)

        val imagePixels = longArrayOf(
            0xff05_0505, 0xff00_0000, 0xff00_0000, 0xff00_0000, 0xff00_0000, 0xff09_0909, 0xff06_0404,
            0xff03_0102, 0xff08_0607, 0xff07_0506, 0xff01_0001, 0xff07_0506, 0xff36_4341, 0xff22_3529,
            0xff14_251c, 0xff11_221a, 0xff1f_3020, 0xff34_443a, 0xff64_817e, 0xff63_8777, 0xff48_6d58,
            0xff2f_5536, 0xff46_7258, 0xff7f_b7b9, 0xff6d_8473, 0xff85_9488, 0xff7a_947e, 0xff5f_815d,
            0xff3a_5d46, 0xff49_7469, 0xff73_7a73, 0xff65_6453, 0xff44_5938, 0xff65_7c4b, 0xff65_715b,
            0xff6a_816e, 0xff66_7366, 0xff5b_5547, 0xff3b_391e, 0xff70_5e3d, 0xff7f_6c5e, 0xff6d_7c6c,
            0xffa9_9c9c, 0xff8b_7671, 0xff6a_3229, 0xff80_514b, 0xff85_7970, 0xff4f_5a4c, 0xff89_7273,
            0xff74_5451, 0xff51_2823, 0xff78_585a, 0xff53_5552, 0xff40_493f, 0xff15_1616, 0xff0a_0c0c,
            0xff05_0808, 0xff01_0303, 0xff00_0100, 0xff01_0200, 0xff19_1816, 0xff18_1818, 0xff0c_0c0c,
            0xff04_0404, 0xff0c_0c0c, 0xff15_1514, 0xffb1_c3b9, 0xffbf_bfbf, 0xffba_baba, 0xffb7_b7b7,
            0xffb3_b3b3, 0xffad_adad, 0xff53_5756, 0xff57_5656, 0xff55_5555, 0xff55_5555, 0xff54_5454,
            0xff47_4646, 0xff00_0000, 0xff00_0000, 0xff0b_0b0b, 0xff0b_0b0b, 0xff00_0000, 0xff00_0000,
        ).map { it.toInt() }.toIntArray()
        val count = 16

        val result1 = QuantizerCelebi.quantize(imagePixels, count).toList()
        val result2 = QuantizerCelebi.quantize(imagePixels, count).toList()
        assertContentEquals(result1, result2)

        val result3 = com.kyant.aura.core.quantize.QuantizerCelebi.quantize(
            intArrayOf(red, red, green, green, green),
            maxColors
        )
        assertEquals(result3.keys.size, 2)
        assertEquals(result3.getValue(green), 3)
        assertEquals(result3.getValue(red), 2)

        val result4 = com.kyant.aura.core.quantize.QuantizerCelebi.quantize(imagePixels, count).toList()
        val result5 = com.kyant.aura.core.quantize.QuantizerCelebi.quantize(imagePixels, count).toList()
        assertContentEquals(result4, result5)
        assertEquals(result, result3)
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
