# Aura

Aura is a material 3 color scheme library based on Google's material-color-utilities,
optimized for speed and performance.

Aura supports **Android Views** (mdc-android) and **Jetpack Compose** (material3).

APIs are unavailable yet.

## Import

[![JitPack Release](https://jitpack.io/v/Kyant0/aura.svg)](https://jitpack.io/#Kyant0/Aura)

```kotlin
// build.gradle.kts
allprojects {
    repositories {
        maven("https://jitpack.io")
    }
}

// app/build.gradle.kts
implementation("com.github.Kyant0.Aura:aura-core:<version>") // core
implementation("com.github.Kyant0.Aura:aura-views:<version>") // Android Views
implementation("com.github.Kyant0.Aura:aura-compose:<version>") // Jetpack Compose
```

## Jetpack Compose usage

```kotlin
import com.kyant.aura.compose.AuraMaterialTheme
import com.kyant.aura.compose.AuraSchemeOptions

val schemeOptions = AuraSchemeOptions.Default/*.copy(
    sourceColor = sourceColor,
    variant = Variant.TONAL_SPOT,
    isDark = isDark,
    contrastLevel = 0.0f,
)*/

AuraMaterialTheme(schemeOptions = schemeOptions) {
    val primaryColor = MaterialTheme.colorScheme.primary
}
```

or

```kotlin
import com.kyant.aura.compose.AuraSchemeOptions

val schemeOptions = AuraSchemeOptions.Default

MaterialTheme(colorScheme = schemeOptions.asAuraColorScheme()) {
}
```

## Android Views usage

* Activity:

You can write once to apply to all activities:

```kotlin
import com.kyant.aura.views.dynamic.ContentBasedDynamicColorSource
import com.kyant.aura.views.dynamic.DynamicColors
import com.kyant.aura.views.dynamic.DynamicColorsOptions

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(
            this,
            DynamicColorsOptions(
                dynamicColorSource = ContentBasedDynamicColorSource(sourceColor),
                variant = Variant.TONAL_SPOT,
                /* ... */
            )
        )
    }
}
```

or in every activity you want to apply:

```kotlin
import com.kyant.aura.views.dynamic.DynamicColors
import com.kyant.aura.views.dynamic.DynamicColorsOptions

class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(
            this,
            DynamicColorsOptions()
        )
        super.onCreate(savedInstanceState)
    }
}
```

* Fragment, View:

```kotlin
import com.kyant.aura.views.dynamic.DynamicColors

val context = DynamicColors.wrapContextIfAvailable(context)
```

* More examples please refer
  to [Using dynamic colors](https://github.com/material-components/material-components-android/blob/master/docs/theming/Color.md#using-dynamic-colors)

## Common usage

```kotlin
val hct = Hct(hue, chroma, tone)
val argb: Int = hct.asArgb()
```

## Benchmarks

```
        4,135   ns          45 allocs    googleHct
        2,452   ns           1 allocs    auraHct

        2,284   ns          18 allocs    googleHct2
        1,490   ns           2 allocs    auraHct2

      490,223   ns        5830 allocs    googlePalette
      219,272   ns        1022 allocs    auraPalette

    2,814,468   ns       27628 allocs    googleFidelityPalette
    1,606,676   ns        2125 allocs    auraFidelityPalette

      734,548   ns         504 allocs    googleQuantize
      809,711   ns         492 allocs    auraQuantize

      852,304   ns        1227 allocs    googleQuantize2
      902,532   ns        1097 allocs    auraQuantize2
```
