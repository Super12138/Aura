/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kyant.aura.core.quantize

import com.kyant.aura.core.utils.ColorUtils

/**
 * An image quantizer that divides the image's pixels into clusters by recursively cutting an RGB
 * cube, based on the weight of pixels in each area of the cube.
 *
 *
 * The algorithm was described by Xiaolin Wu in Graphic Gems II, published in 1991.
 */
class QuantizerWu : Quantizer {
    private var weights: IntArray = intArrayOf()
    private var momentsR: IntArray = intArrayOf()
    private var momentsG: IntArray = intArrayOf()
    private var momentsB: IntArray = intArrayOf()
    private var moments: DoubleArray = doubleArrayOf()
    private var cubes: Array<Box> = emptyArray()

    override fun quantize(pixels: IntArray, colorCount: Int): QuantizerResult {
        val mapResult = QuantizerMap().quantize(pixels, colorCount)
        constructHistogram(mapResult.colorToCount)
        createMoments()
        val createBoxesResult = createBoxes(colorCount)
        val resultMap: MutableMap<Int, Int> = LinkedHashMap()
        for (i in 0..<createBoxesResult) {
            val cube = cubes[i]
            val weight = volume(cube, weights)
            if (weight > 0) {
                val r = volume(cube, momentsR) / weight
                val g = volume(cube, momentsG) / weight
                val b = volume(cube, momentsB) / weight
                val color = ColorUtils.argbFromRgb(r, g, b)
                resultMap.put(color, 0)
            }
        }
        return QuantizerResult(resultMap)
    }

    private fun constructHistogram(pixels: Map<Int, Int>) {
        weights = IntArray(TOTAL_SIZE)
        momentsR = IntArray(TOTAL_SIZE)
        momentsG = IntArray(TOTAL_SIZE)
        momentsB = IntArray(TOTAL_SIZE)
        moments = DoubleArray(TOTAL_SIZE)

        for (pair in pixels.entries) {
            val pixel: Int = pair.key
            val count: Int = pair.value
            val red = pixel shr 16 and 0xFF
            val green = pixel shr 8 and 0xFF
            val blue = pixel and 0xFF
            val bitsToRemove: Int = 8 - INDEX_BITS
            val iR = (red shr bitsToRemove) + 1
            val iG = (green shr bitsToRemove) + 1
            val iB = (blue shr bitsToRemove) + 1
            val index: Int = getIndex(iR, iG, iB)
            weights[index] += count
            momentsR[index] += (red * count)
            momentsG[index] += (green * count)
            momentsB[index] += (blue * count)
            moments[index] += (count * ((red * red) + (green * green) + (blue * blue))).toDouble()
        }
    }

    private fun createMoments() {
        for (r in 1..<INDEX_COUNT) {
            val area = IntArray(INDEX_COUNT)
            val areaR = IntArray(INDEX_COUNT)
            val areaG = IntArray(INDEX_COUNT)
            val areaB = IntArray(INDEX_COUNT)
            val area2 = DoubleArray(INDEX_COUNT)

            for (g in 1..<INDEX_COUNT) {
                var line = 0
                var lineR = 0
                var lineG = 0
                var lineB = 0
                var line2 = 0.0
                for (b in 1..<INDEX_COUNT) {
                    val index: Int = getIndex(r, g, b)
                    line += weights[index]
                    lineR += momentsR[index]
                    lineG += momentsG[index]
                    lineB += momentsB[index]
                    line2 += moments[index]

                    area[b] += line
                    areaR[b] += lineR
                    areaG[b] += lineG
                    areaB[b] += lineB
                    area2[b] += line2

                    val previousIndex: Int = getIndex(r - 1, g, b)
                    weights[index] = weights[previousIndex] + area[b]
                    momentsR[index] = momentsR[previousIndex] + areaR[b]
                    momentsG[index] = momentsG[previousIndex] + areaG[b]
                    momentsB[index] = momentsB[previousIndex] + areaB[b]
                    moments[index] = moments[previousIndex] + area2[b]
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun createBoxes(maxColorCount: Int): Int {
        cubes = arrayOfNulls<Box>(maxColorCount) as Array<Box>
        for (i in 0..<maxColorCount) {
            cubes[i] = Box()
        }
        val volumeVariance = DoubleArray(maxColorCount)
        val firstBox = cubes[0]
        firstBox.r1 = INDEX_COUNT - 1
        firstBox.g1 = INDEX_COUNT - 1
        firstBox.b1 = INDEX_COUNT - 1

        var generatedColorCount = maxColorCount
        var next = 0
        var i = 1
        while (i < maxColorCount) {
            if (cut(cubes[next], cubes[i])) {
                volumeVariance[next] = if (cubes[next].vol > 1) variance(cubes[next]) else 0.0
                volumeVariance[i] = if (cubes[i].vol > 1) variance(cubes[i]) else 0.0
            } else {
                volumeVariance[next] = 0.0
                i--
            }

            next = 0

            var temp = volumeVariance[0]
            for (j in 1..i) {
                if (volumeVariance[j] > temp) {
                    temp = volumeVariance[j]
                    next = j
                }
            }
            if (temp <= 0.0) {
                generatedColorCount = i + 1
                break
            }
            i++
        }

        return generatedColorCount
    }

    private fun variance(cube: Box): Double {
        val dr = volume(cube, momentsR)
        val dg = volume(cube, momentsG)
        val db = volume(cube, momentsB)
        val xx =
            ((((moments[getIndex(cube.r1, cube.g1, cube.b1)]
                    - moments[getIndex(cube.r1, cube.g1, cube.b0)]
                    - moments[getIndex(cube.r1, cube.g0, cube.b1)])
                    + moments[getIndex(cube.r1, cube.g0, cube.b0)]
                    - moments[getIndex(cube.r0, cube.g1, cube.b1)])
                    + moments[getIndex(cube.r0, cube.g1, cube.b0)]
                    + moments[getIndex(cube.r0, cube.g0, cube.b1)])
                    - moments[getIndex(cube.r0, cube.g0, cube.b0)])

        val hypotenuse = dr * dr + dg * dg + db * db
        val volume = volume(cube, weights)
        return xx - hypotenuse / volume.toDouble()
    }

    private fun cut(one: Box, two: Box): Boolean {
        val wholeR = volume(one, momentsR)
        val wholeG = volume(one, momentsG)
        val wholeB = volume(one, momentsB)
        val wholeW = volume(one, weights)

        val maxRResult = maximize(one, Direction.RED, one.r0 + 1, one.r1, wholeR, wholeG, wholeB, wholeW)
        val maxGResult = maximize(one, Direction.GREEN, one.g0 + 1, one.g1, wholeR, wholeG, wholeB, wholeW)
        val maxBResult = maximize(one, Direction.BLUE, one.b0 + 1, one.b1, wholeR, wholeG, wholeB, wholeW)
        val maxR = maxRResult.maximum
        val maxG = maxGResult.maximum
        val maxB = maxBResult.maximum
        val cutDirection =
            if (maxR >= maxG && maxR >= maxB) {
                if (maxRResult.cutLocation < 0) {
                    return false
                }
                Direction.RED
            } else if (maxG >= maxR && maxG >= maxB) {
                Direction.GREEN
            } else {
                Direction.BLUE
            }

        two.r1 = one.r1
        two.g1 = one.g1
        two.b1 = one.b1

        when (cutDirection) {
            Direction.RED -> {
                one.r1 = maxRResult.cutLocation
                two.r0 = one.r1
                two.g0 = one.g0
                two.b0 = one.b0
            }

            Direction.GREEN -> {
                one.g1 = maxGResult.cutLocation
                two.r0 = one.r0
                two.g0 = one.g1
                two.b0 = one.b0
            }

            Direction.BLUE -> {
                one.b1 = maxBResult.cutLocation
                two.r0 = one.r0
                two.g0 = one.g0
                two.b0 = one.b1
            }
        }

        one.vol = (one.r1 - one.r0) * (one.g1 - one.g0) * (one.b1 - one.b0)
        two.vol = (two.r1 - two.r0) * (two.g1 - two.g0) * (two.b1 - two.b0)

        return true
    }

    private fun maximize(
        cube: Box,
        direction: Direction,
        first: Int,
        last: Int,
        wholeR: Int,
        wholeG: Int,
        wholeB: Int,
        wholeW: Int
    ): MaximizeResult {
        val bottomR = bottom(cube, direction, momentsR)
        val bottomG = bottom(cube, direction, momentsG)
        val bottomB = bottom(cube, direction, momentsB)
        val bottomW = bottom(cube, direction, weights)

        var max = 0.0
        var cut = -1

        var halfR: Int
        var halfG: Int
        var halfB: Int
        var halfW: Int
        for (i in first..<last) {
            halfW = bottomW + top(cube, direction, i, weights)
            if (halfW == 0 || wholeW == halfW) {
                continue
            }
            halfR = bottomR + top(cube, direction, i, momentsR)
            halfG = bottomG + top(cube, direction, i, momentsG)
            halfB = bottomB + top(cube, direction, i, momentsB)

            var tempNumerator = (halfR * halfR + halfG * halfG + halfB * halfB).toDouble()
            var tempDenominator = halfW.toDouble()
            var temp = tempNumerator / tempDenominator

            halfR = wholeR - halfR
            halfG = wholeG - halfG
            halfB = wholeB - halfB
            halfW = wholeW - halfW

            tempNumerator = (halfR * halfR + halfG * halfG + halfB * halfB).toDouble()
            tempDenominator = halfW.toDouble()
            temp += tempNumerator / tempDenominator

            if (temp > max) {
                max = temp
                cut = i
            }
        }
        return MaximizeResult(cut, max)
    }

    private enum class Direction {
        RED,
        GREEN,
        BLUE
    }

    private class MaximizeResult(
        // < 0 if cut impossible
        var cutLocation: Int,
        var maximum: Double
    )

    private class Box(
        var r0: Int = 0,
        var r1: Int = 0,
        var g0: Int = 0,
        var g1: Int = 0,
        var b0: Int = 0,
        var b1: Int = 0,
        var vol: Int = 0
    )

    companion object {
        // A histogram of all the input colors is constructed. It has the shape of a
        // cube. The cube would be too large if it contained all 16 million colors:
        // historical best practice is to use 5 bits  of the 8 in each channel,
        // reducing the histogram to a volume of ~32,000.
        private const val INDEX_BITS = 5
        private const val INDEX_COUNT = 33 // ((1 << INDEX_BITS) + 1)
        private const val TOTAL_SIZE = 35937 // INDEX_COUNT * INDEX_COUNT * INDEX_COUNT

        @JvmStatic
        private fun getIndex(r: Int, g: Int, b: Int): Int {
            return (r shl (INDEX_BITS * 2)) + (r shl (INDEX_BITS + 1)) + r + (g shl INDEX_BITS) + g + b
        }

        @JvmStatic
        private fun volume(cube: Box, moment: IntArray): Int {
            return ((((moment[getIndex(cube.r1, cube.g1, cube.b1)]
                    - moment[getIndex(cube.r1, cube.g1, cube.b0)]
                    - moment[getIndex(cube.r1, cube.g0, cube.b1)])
                    + moment[getIndex(cube.r1, cube.g0, cube.b0)]
                    - moment[getIndex(cube.r0, cube.g1, cube.b1)])
                    + moment[getIndex(cube.r0, cube.g1, cube.b0)]
                    + moment[getIndex(cube.r0, cube.g0, cube.b1)])
                    - moment[getIndex(cube.r0, cube.g0, cube.b0)])
        }

        @JvmStatic
        private fun bottom(cube: Box, direction: Direction, moment: IntArray): Int {
            return when (direction) {
                Direction.RED -> ((-moment[getIndex(cube.r0, cube.g1, cube.b1)]
                        + moment[getIndex(cube.r0, cube.g1, cube.b0)]
                        + moment[getIndex(cube.r0, cube.g0, cube.b1)])
                        - moment[getIndex(cube.r0, cube.g0, cube.b0)])

                Direction.GREEN -> ((-moment[getIndex(cube.r1, cube.g0, cube.b1)]
                        + moment[getIndex(cube.r1, cube.g0, cube.b0)]
                        + moment[getIndex(cube.r0, cube.g0, cube.b1)])
                        - moment[getIndex(cube.r0, cube.g0, cube.b0)])

                Direction.BLUE -> ((-moment[getIndex(cube.r1, cube.g1, cube.b0)]
                        + moment[getIndex(cube.r1, cube.g0, cube.b0)]
                        + moment[getIndex(cube.r0, cube.g1, cube.b0)])
                        - moment[getIndex(cube.r0, cube.g0, cube.b0)])
            }
        }

        @JvmStatic
        private fun top(cube: Box, direction: Direction, position: Int, moment: IntArray): Int {
            return when (direction) {
                Direction.RED -> ((moment[getIndex(position, cube.g1, cube.b1)]
                        - moment[getIndex(position, cube.g1, cube.b0)]
                        - moment[getIndex(position, cube.g0, cube.b1)])
                        + moment[getIndex(position, cube.g0, cube.b0)])

                Direction.GREEN -> ((moment[getIndex(cube.r1, position, cube.b1)]
                        - moment[getIndex(cube.r1, position, cube.b0)]
                        - moment[getIndex(cube.r0, position, cube.b1)])
                        + moment[getIndex(cube.r0, position, cube.b0)])

                Direction.BLUE -> ((moment[getIndex(cube.r1, cube.g1, position)]
                        - moment[getIndex(cube.r1, cube.g0, position)]
                        - moment[getIndex(cube.r0, cube.g1, position)])
                        + moment[getIndex(cube.r0, cube.g0, position)])
            }
        }
    }
}
