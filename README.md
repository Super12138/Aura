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
        4,135   ns          45 allocs    googleHct
        2,452   ns           1 allocs    auraHct

        2,284   ns          18 allocs    googleHct2
        1,490   ns           2 allocs    auraHct2

      490,223   ns        5830 allocs    googlePalette
      219,272   ns        1022 allocs    auraPalette

    2,809,882   ns       27627 allocs    googleFidelityPalette
    1,613,048   ns        2103 allocs    auraFidelityPalette

      734,548   ns         504 allocs    googleQuantize
      809,711   ns         492 allocs    auraQuantize

      852,304   ns        1227 allocs    googleQuantize2
      902,532   ns        1097 allocs    auraQuantize2
```
