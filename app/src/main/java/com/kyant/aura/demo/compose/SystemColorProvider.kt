package com.kyant.aura.demo.compose

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.S)
class SystemColorProvider(context: Context) : ColorProvider() {
    override val neutral1: IntArray = intArrayOf(
        context.getColor(android.R.color.system_neutral1_0),
        context.getColor(android.R.color.system_neutral1_10),
        context.getColor(android.R.color.system_neutral1_50),
        context.getColor(android.R.color.system_neutral1_100),
        context.getColor(android.R.color.system_neutral1_200),
        context.getColor(android.R.color.system_neutral1_300),
        context.getColor(android.R.color.system_neutral1_400),
        context.getColor(android.R.color.system_neutral1_500),
        context.getColor(android.R.color.system_neutral1_600),
        context.getColor(android.R.color.system_neutral1_700),
        context.getColor(android.R.color.system_neutral1_800),
        context.getColor(android.R.color.system_neutral1_900),
        context.getColor(android.R.color.system_neutral1_1000)
    )

    override val neutral2: IntArray = intArrayOf(
        context.getColor(android.R.color.system_neutral2_0),
        context.getColor(android.R.color.system_neutral2_10),
        context.getColor(android.R.color.system_neutral2_50),
        context.getColor(android.R.color.system_neutral2_100),
        context.getColor(android.R.color.system_neutral2_200),
        context.getColor(android.R.color.system_neutral2_300),
        context.getColor(android.R.color.system_neutral2_400),
        context.getColor(android.R.color.system_neutral2_500),
        context.getColor(android.R.color.system_neutral2_600),
        context.getColor(android.R.color.system_neutral2_700),
        context.getColor(android.R.color.system_neutral2_800),
        context.getColor(android.R.color.system_neutral2_900),
        context.getColor(android.R.color.system_neutral2_1000)
    )

    override val accent1: IntArray = intArrayOf(
        context.getColor(android.R.color.system_accent1_0),
        context.getColor(android.R.color.system_accent1_10),
        context.getColor(android.R.color.system_accent1_50),
        context.getColor(android.R.color.system_accent1_100),
        context.getColor(android.R.color.system_accent1_200),
        context.getColor(android.R.color.system_accent1_300),
        context.getColor(android.R.color.system_accent1_400),
        context.getColor(android.R.color.system_accent1_500),
        context.getColor(android.R.color.system_accent1_600),
        context.getColor(android.R.color.system_accent1_700),
        context.getColor(android.R.color.system_accent1_800),
        context.getColor(android.R.color.system_accent1_900),
        context.getColor(android.R.color.system_accent1_1000)
    )

    override val accent2: IntArray = intArrayOf(
        context.getColor(android.R.color.system_accent2_0),
        context.getColor(android.R.color.system_accent2_10),
        context.getColor(android.R.color.system_accent2_50),
        context.getColor(android.R.color.system_accent2_100),
        context.getColor(android.R.color.system_accent2_200),
        context.getColor(android.R.color.system_accent2_300),
        context.getColor(android.R.color.system_accent2_400),
        context.getColor(android.R.color.system_accent2_500),
        context.getColor(android.R.color.system_accent2_600),
        context.getColor(android.R.color.system_accent2_700),
        context.getColor(android.R.color.system_accent2_800),
        context.getColor(android.R.color.system_accent2_900),
        context.getColor(android.R.color.system_accent2_1000)
    )

    override val accent3: IntArray = intArrayOf(
        context.getColor(android.R.color.system_accent3_0),
        context.getColor(android.R.color.system_accent3_10),
        context.getColor(android.R.color.system_accent3_50),
        context.getColor(android.R.color.system_accent3_100),
        context.getColor(android.R.color.system_accent3_200),
        context.getColor(android.R.color.system_accent3_300),
        context.getColor(android.R.color.system_accent3_400),
        context.getColor(android.R.color.system_accent3_500),
        context.getColor(android.R.color.system_accent3_600),
        context.getColor(android.R.color.system_accent3_700),
        context.getColor(android.R.color.system_accent3_800),
        context.getColor(android.R.color.system_accent3_900),
        context.getColor(android.R.color.system_accent3_1000)
    )
}
