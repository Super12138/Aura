package com.kyant.aura.demo.views

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.kyant.aura.core.dynamiccolor.Variant
import com.kyant.aura.demo.R
import com.kyant.aura.views.dynamic.ContentBasedDynamicColorSource
import com.kyant.aura.views.dynamic.DynamicColors
import com.kyant.aura.views.dynamic.DynamicColorsOptions

class ViewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(
            this,
            DynamicColorsOptions(
                dynamicColorSource = ContentBasedDynamicColorSource(0xFF03DAC6.toInt()),
                variant = Variant.TONAL_SPOT
            )
        )
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }
}
