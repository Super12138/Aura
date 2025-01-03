package com.kyant.aura.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kyant.m3color.dynamiccolor.MaterialDynamicColors
import com.kyant.m3color.hct.Hct
import com.kyant.m3color.scheme.SchemeFidelity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Benchmarks {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

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
}
