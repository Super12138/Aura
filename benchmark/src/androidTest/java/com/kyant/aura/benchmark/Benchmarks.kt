package com.kyant.aura.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kyant.m3color.dynamiccolor.MaterialDynamicColors
import com.kyant.m3color.hct.Hct
import com.kyant.m3color.scheme.SchemeExpressive
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
}
