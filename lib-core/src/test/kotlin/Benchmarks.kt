import com.kyant.m3color.dynamiccolor.MaterialDynamicColors
import com.kyant.m3color.hct.Hct
import com.kyant.m3color.scheme.SchemeExpressive
import kotlinx.benchmark.*

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 500, timeUnit = BenchmarkTimeUnit.MILLISECONDS)
@Measurement(iterations = 8, time = 500, timeUnit = BenchmarkTimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
class Benchmarks {
    @Setup
    fun prepare() {
    }

    @TearDown
    fun cleanup() {
    }

    @Benchmark
    fun googleHct() {
        Hct.from(100.0, 1000.0, 50.0)
    }

    @Benchmark
    fun auraHct() {
        com.kyant.aura.core.hct.Hct(100.0, 1000.0, 50.0)
    }

    @Benchmark
    fun googleHct2() {
        Hct.fromInt(0x6200EE).run {
            Hct.from(hue, chroma, tone)
        }
    }

    @Benchmark
    fun auraHct2() {
        com.kyant.aura.core.hct.Hct(0x6200EE).run {
            com.kyant.aura.core.hct.Hct(hue, chroma, tone)
        }
    }

    @Benchmark
    fun googlePalette() {
        val scheme = SchemeExpressive(Hct.fromInt(0x6200EE), false, 0.5)
        val mdc = MaterialDynamicColors()
        mdc.allDynamicColors().forEach {
            scheme.getHct(it.get())
        }
    }

    @Benchmark
    fun auraPalette() {
        val scheme = com.kyant.aura.core.scheme.SchemeExpressive(com.kyant.aura.core.hct.Hct(0x6200EE), false, 0.5)
        val mdc = com.kyant.aura.core.dynamiccolor.MaterialDynamicColors()
        mdc.allDynamicColors().forEach {
            scheme.getHct(it.invoke())
        }
    }
}
