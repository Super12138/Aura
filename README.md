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
        4,300   ns          45 allocs    Trace    Method Trace    Benchmarks.googleHct
        2,511   ns           1 allocs    Trace    Method Trace    Benchmarks.auraHct

        2,265   ns          18 allocs    Trace    Method Trace    Benchmarks.googleHct2
        1,497   ns           3 allocs    Trace    Method Trace    Benchmarks.auraHct2

      503,186   ns        5831 allocs    Trace    Method Trace    Benchmarks.googlePalette
      296,384   ns        1459 allocs    Trace    Method Trace    Benchmarks.auraPalette
```
