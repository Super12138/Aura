package com.kyant.aura.demo.compose

sealed class ColorProvider {
    abstract val neutral1: IntArray
    abstract val neutral2: IntArray
    abstract val accent1: IntArray
    abstract val accent2: IntArray
    abstract val accent3: IntArray

    val colors: Array<IntArray>
        get() = arrayOf(neutral1, neutral2, accent1, accent2, accent3)

    val colorNames = arrayOf("N1", "N2", "A1", "A2", "A3")

    val shadeNumbers = intArrayOf(0, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
}
