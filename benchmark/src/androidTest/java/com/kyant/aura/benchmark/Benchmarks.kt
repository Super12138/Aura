package com.kyant.aura.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kyant.m3color.dynamiccolor.MaterialDynamicColors
import com.kyant.m3color.hct.Hct
import com.kyant.m3color.quantize.QuantizerCelebi
import com.kyant.m3color.scheme.SchemeExpressive
import com.kyant.m3color.scheme.SchemeFidelity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Benchmarks {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun googleHct() {
        benchmarkRule.measureRepeated {
            Hct.from(100.0, 1000.0, 50.0)
        }
    }

    @Test
    fun auraHct() {
        benchmarkRule.measureRepeated {
            com.kyant.aura.core.hct.Hct(100.0, 1000.0, 50.0)
        }
    }

    @Test
    fun googleHct2() {
        benchmarkRule.measureRepeated {
            val hct = Hct.fromInt(0x6200EE)
            Hct.from(hct.hue, hct.chroma, hct.tone)
        }
    }

    @Test
    fun auraHct2() {
        benchmarkRule.measureRepeated {
            val hct = com.kyant.aura.core.hct.Hct(0x6200EE)
            com.kyant.aura.core.hct.Hct(hct.hue, hct.chroma, hct.tone)
        }
    }

    @Test
    fun googlePalette() {
        benchmarkRule.measureRepeated {
            val scheme = SchemeExpressive(Hct.fromInt(0x6200EE), false, 0.5)
            val mdc = MaterialDynamicColors()
            val colors = mdc.allDynamicColors()
            for (i in colors.indices) {
                scheme.getHct(colors[i].get())
            }
        }
    }

    @Test
    fun auraPalette() {
        benchmarkRule.measureRepeated {
            val scheme = com.kyant.aura.core.scheme.SchemeExpressive(com.kyant.aura.core.hct.Hct(0x6200EE), false, 0.5)
            val mdc = com.kyant.aura.core.dynamiccolor.MaterialDynamicColors()
            val colors = mdc.allDynamicColors()
            for (i in colors.indices) {
                scheme.getHct(colors[i].invoke())
            }
        }
    }

    @Test
    fun googleFidelityPalette() {
        benchmarkRule.measureRepeated {
            val scheme = SchemeFidelity(Hct.fromInt(0x6200EE), false, 0.5)
            val mdc = MaterialDynamicColors()
            val colors = mdc.allDynamicColors()
            for (i in colors.indices) {
                scheme.getHct(colors[i].get())
            }
        }
    }

    @Test
    fun auraFidelityPalette() {
        benchmarkRule.measureRepeated {
            val scheme = com.kyant.aura.core.scheme.SchemeFidelity(com.kyant.aura.core.hct.Hct(0x6200EE), false, 0.5)
            val mdc = com.kyant.aura.core.dynamiccolor.MaterialDynamicColors()
            val colors = mdc.allDynamicColors()
            for (i in colors.indices) {
                scheme.getHct(colors[i].invoke())
            }
        }
    }

    val red = 0xffff_0000.toInt()
    val green = 0xff00_ff00.toInt()
    val maxColors = 256

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

    @Test
    fun googleQuantize() {
        benchmarkRule.measureRepeated {
            QuantizerCelebi.quantize(intArrayOf(red, red, green, green, green), maxColors)
        }
    }

    @Test
    fun googleQuantize2() {
        benchmarkRule.measureRepeated {
            QuantizerCelebi.quantize(imagePixels, count)
        }
    }

    @Test
    fun auraQuantize() {
        benchmarkRule.measureRepeated {
            com.kyant.aura.core.quantize.QuantizerCelebi.quantize(intArrayOf(red, red, green, green, green), maxColors)
        }
    }

    @Test
    fun auraQuantize2() {
        benchmarkRule.measureRepeated {
            com.kyant.aura.core.quantize.QuantizerCelebi.quantize(imagePixels, count)
        }
    }
}
