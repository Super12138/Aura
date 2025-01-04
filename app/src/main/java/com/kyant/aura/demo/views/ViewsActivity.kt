package com.kyant.aura.demo.views

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.kyant.aura.core.dynamiccolor.Variant
import com.kyant.aura.demo.R
import com.kyant.aura.views.dynamic.ContentBasedDynamicColorSource
import com.kyant.aura.views.dynamic.DynamicColors
import com.kyant.aura.views.dynamic.DynamicColorsOptions
import kotlin.random.Random

class ViewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(
            this,
            DynamicColorsOptions(
                dynamicColorSource = ContentBasedDynamicColorSource(color),
                variant = Variant.TONAL_SPOT
            )
        )
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val anotherThemeContainer = findViewById<FrameLayout>(R.id.another_theme_container)
        val anotherThemeContext = DynamicColors.wrapContextIfAvailable(
            this, DynamicColorsOptions(
                dynamicColorSource = ContentBasedDynamicColorSource(color),
                variant = Variant.EXPRESSIVE
            )
        )
        val anotherThemeButton = MaterialButton(anotherThemeContext)
        anotherThemeButton.text = "This is another theme"
        anotherThemeContainer.addView(anotherThemeButton)

        val changeTheme = findViewById<Button>(R.id.btn_change_theme)
        changeTheme.setOnClickListener {
            color = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
            recreate()
        }
    }

    private companion object {
        var color = 0xFF03DAC6.toInt()
    }
}
