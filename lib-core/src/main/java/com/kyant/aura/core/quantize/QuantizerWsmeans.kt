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

import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * An image quantizer that improves on the speed of a standard K-Means algorithm by implementing
 * several optimizations, including deduping identical pixels and a triangle inequality rule that
 * reduces the number of comparisons needed to identify which cluster a point should be moved to.
 *
 *
 * Wsmeans stands for Weighted Square Means.
 *
 *
 * This algorithm was designed by M. Emre Celebi, and was found in their 2011 paper, Improving
 * the Performance of K-Means for Color Quantization. https://arxiv.org/abs/1101.0395
 */
object QuantizerWsmeans {
    private const val MAX_ITERATIONS = 10
    private const val MIN_MOVEMENT_DISTANCE = 3.0

    /**
     * Reduce the number of colors needed to represented the input, minimizing the difference between
     * the original image and the recolored image.
     *
     * @param inputPixels      Colors in ARGB format.
     * @param startingClusters Defines the initial state of the quantizer. Passing an empty array is
     * fine, the implementation will create its own initial state that leads to reproducible
     * results for the same inputs. Passing an array that is the result of Wu quantization leads
     * to higher quality results.
     * @param maxColors        The number of colors to divide the image into. A lower number of colors may be
     * returned.
     * @return Map with keys of colors in ARGB format, values of how many of the input pixels belong
     * to the color.
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun quantize(inputPixels: IntArray, startingClusters: IntArray, maxColors: Int): Map<Int, Int> {
        // Uses a seeded random number generator to ensure consistent results.
        val random = Random(0x42688)

        val pixelToCount: MutableMap<Int, Int> = LinkedHashMap()
        val points: Array<DoubleArray> = arrayOfNulls<DoubleArray>(inputPixels.size) as Array<DoubleArray>
        val pixels = IntArray(inputPixels.size)

        var pointCount = 0
        for (i in inputPixels.indices) {
            val inputPixel = inputPixels[i]
            val pixelCount = pixelToCount[inputPixel]
            if (pixelCount == null) {
                points[pointCount] = CieLab.fromInt(inputPixel)
                pixels[pointCount] = inputPixel
                pointCount++

                pixelToCount.put(inputPixel, 1)
            } else {
                pixelToCount.put(inputPixel, pixelCount + 1)
            }
        }

        val clusterCount =
            if (startingClusters.isNotEmpty()) {
                minOf(maxColors, pointCount, startingClusters.size)
            } else {
                min(maxColors, pointCount)
            }

        val clusters: Array<DoubleArray> = arrayOfNulls<DoubleArray>(clusterCount) as Array<DoubleArray>
        for (i in startingClusters.indices) {
            clusters[i] = CieLab.fromInt(startingClusters[i])
        }

        val clusterIndices = IntArray(pointCount)
        for (i in 0..<pointCount) {
            clusterIndices[i] = random.nextInt(clusterCount)
        }

        val distanceToIndexMatrix: Array<Array<Distance>> =
            Array(clusterCount) {
                Array(clusterCount) {
                    Distance()
                }
            }

        val pixelCountSums = IntArray(clusterCount)
        for (iteration in 0..<MAX_ITERATIONS) {
            for (i in 0..<clusterCount) {
                for (j in i + 1..<clusterCount) {
                    val distance = CieLab.distance(clusters[i], clusters[j])
                    distanceToIndexMatrix[j][i].distance = distance
                    distanceToIndexMatrix[j][i].index = i
                    distanceToIndexMatrix[i][j].distance = distance
                    distanceToIndexMatrix[i][j].index = j
                }
                distanceToIndexMatrix[i].sort()
            }

            var pointsMoved = 0
            for (i in 0..<pointCount) {
                val point = points[i]
                val previousClusterIndex = clusterIndices[i]
                val previousCluster = clusters[previousClusterIndex]
                val previousDistance = CieLab.distance(point, previousCluster)

                var minimumDistance = previousDistance
                var newClusterIndex = -1
                for (j in 0..<clusterCount) {
                    if (distanceToIndexMatrix[previousClusterIndex][j].distance >= 4 * previousDistance) {
                        continue
                    }
                    val distance = CieLab.distance(point, clusters[j])
                    if (distance < minimumDistance) {
                        minimumDistance = distance
                        newClusterIndex = j
                    }
                }
                if (newClusterIndex != -1) {
                    val distanceChange = abs(sqrt(minimumDistance) - sqrt(previousDistance))
                    if (distanceChange > MIN_MOVEMENT_DISTANCE) {
                        pointsMoved++
                        clusterIndices[i] = newClusterIndex
                    }
                }
            }

            if (pointsMoved == 0 && iteration != 0) {
                break
            }

            val componentASums = DoubleArray(clusterCount)
            val componentBSums = DoubleArray(clusterCount)
            val componentCSums = DoubleArray(clusterCount)
            for (i in 0..<pointCount) {
                val clusterIndex = clusterIndices[i]
                val point = points[i]
                val count = pixelToCount.getValue(pixels[i])
                pixelCountSums[clusterIndex] += count
                componentASums[clusterIndex] += point[0] * count
                componentBSums[clusterIndex] += point[1] * count
                componentCSums[clusterIndex] += point[2] * count
            }

            for (i in 0..<clusterCount) {
                val count = pixelCountSums[i]
                if (count == 0) {
                    clusters[i] = doubleArrayOf(0.0, 0.0, 0.0)
                    continue
                }
                val a = componentASums[i] / count
                val b = componentBSums[i] / count
                val c = componentCSums[i] / count
                clusters[i][0] = a
                clusters[i][1] = b
                clusters[i][2] = c
            }
        }

        val argbToPopulation: MutableMap<Int, Int> = LinkedHashMap()
        for (i in 0..<clusterCount) {
            val count = pixelCountSums[i]
            if (count == 0) {
                continue
            }

            val possibleNewCluster = CieLab.toInt(clusters[i])
            if (argbToPopulation.containsKey(possibleNewCluster)) {
                continue
            }

            argbToPopulation.put(possibleNewCluster, count)
        }

        return argbToPopulation
    }

    private class Distance : Comparable<Distance> {
        var index: Int = -1
        var distance: Double = -1.0

        override fun compareTo(other: Distance): Int {
            return this.distance.compareTo(other.distance)
        }
    }
}
