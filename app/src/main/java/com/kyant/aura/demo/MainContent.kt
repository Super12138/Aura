package com.kyant.aura.demo

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.aura.demo.compose.ComposeColorSchemeActivity
import com.kyant.aura.demo.compose.ComposeHctGamutActivity
import com.kyant.aura.demo.compose.ComposePaletteActivity
import com.kyant.aura.demo.views.ViewsActivity

@Composable
fun MainContent() {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Text(
            stringResource(R.string.app_name),
            Modifier.padding(24.dp, 102.dp, 24.dp, 24.dp),
            style = MaterialTheme.typography.displaySmall
        )
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                "Views",
                Modifier.padding(top = 24.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
            Button({ context.startActivity(Intent(context, ViewsActivity::class.java)) }) {
                Text("Views world")
            }

            Text(
                "Compose",
                Modifier.padding(top = 24.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
            Button({ context.startActivity(Intent(context, ComposeColorSchemeActivity::class.java)) }) {
                Text("Color scheme")
            }
            Button({ context.startActivity(Intent(context, ComposePaletteActivity::class.java)) }) {
                Text("Palette")
            }
            Button({ context.startActivity(Intent(context, ComposeHctGamutActivity::class.java)) }) {
                Text("HCT gamut")
            }
        }
    }
}
