# Aura

Aura is a material 3 color scheme library based on Google's material-color-utilities,
optimized for speed and performance.

Aura supports **Android Views** (mdc-android) and **Jetpack Compose** (material3).

APIs are unavailable yet.

## Import

## Jetpack Compose usage

```kotlin
AuraMaterialTheme(
    schemeOptions = AuraSchemeOptions.Default/*.copy(
        sourceColor = sourceColor,
        variant = Variant.TONAL_SPOT,
        isDark = isDark,
        contrastLevel = 0.0f,
    )*/
) {
    val primaryColor = MaterialTheme.colorScheme.primary
}
```

## Android Views usage

* Activity:

```kotlin
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

* Fragment, View:

```kotlin
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
        4,245   ns          45 allocs    googleHct
        2,492   ns           1 allocs    auraHct

        2,304   ns          18 allocs    googleHct2
        1,481   ns           2 allocs    auraHct2

      494,566   ns        5830 allocs    googlePalette
      218,391   ns        1023 allocs    auraPalette
```
