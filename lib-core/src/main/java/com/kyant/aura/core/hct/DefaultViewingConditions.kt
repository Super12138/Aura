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
package com.kyant.aura.core.hct

@Suppress("ConstPropertyName")
internal object DefaultViewingConditions {
    const val aw = 29.980997194447333
    const val nbb = 1.0169191804458755
    const val ncb = 1.0169191804458755
    const val c = 0.69
    const val n = 0.18418651851244416
    const val rgbD1 = 1.02117770275752
    const val rgbD2 = 0.9863077294280124
    const val rgbD3 = 0.9339605082802299
    const val fl = 0.3884814537800353
    const val flRoot = 0.7894826179304937
    const val z = 1.909169568483652

    const val alphaCoeff = 0.8834525670408592 // (1.64 - 0.29.pow(vc.n)).pow(0.73)
}
