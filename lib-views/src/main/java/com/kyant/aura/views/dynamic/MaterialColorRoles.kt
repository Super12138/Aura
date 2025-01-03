package com.kyant.aura.views.dynamic

import androidx.annotation.AttrRes
import com.google.android.material.R

@Suppress("unused")
enum class MaterialColorRoles(@AttrRes val resId: Int) {
    Primary(R.attr.colorPrimary),
    OnPrimary(R.attr.colorOnPrimary),
    PrimaryContainer(R.attr.colorPrimaryContainer),
    OnPrimaryContainer(R.attr.colorOnPrimaryContainer),

    Secondary(R.attr.colorSecondary),
    OnSecondary(R.attr.colorOnSecondary),
    SecondaryContainer(R.attr.colorSecondaryContainer),
    OnSecondaryContainer(R.attr.colorOnSecondaryContainer),

    Tertiary(R.attr.colorTertiary),
    OnTertiary(R.attr.colorOnTertiary),
    TertiaryContainer(R.attr.colorTertiaryContainer),
    OnTertiaryContainer(R.attr.colorOnTertiaryContainer),

    Error(R.attr.colorError),
    OnError(R.attr.colorOnError),
    ErrorContainer(R.attr.colorErrorContainer),
    OnErrorContainer(R.attr.colorOnErrorContainer),

    PrimaryFixed(R.attr.colorPrimaryFixed),
    OnPrimaryFixed(R.attr.colorOnPrimaryFixed),
    PrimaryFixedDim(R.attr.colorPrimaryFixedDim),
    OnPrimaryFixedVariant(R.attr.colorOnPrimaryFixedVariant),

    SecondaryFixed(R.attr.colorSecondaryFixed),
    OnSecondaryFixed(R.attr.colorOnSecondaryFixed),
    SecondaryFixedDim(R.attr.colorSecondaryFixedDim),
    OnSecondaryFixedVariant(R.attr.colorOnSecondaryFixedVariant),

    TertiaryFixed(R.attr.colorTertiaryFixed),
    OnTertiaryFixed(R.attr.colorOnTertiaryFixed),
    TertiaryFixedDim(R.attr.colorTertiaryFixedDim),
    OnTertiaryFixedVariant(R.attr.colorOnTertiaryFixedVariant),

    SurfaceDim(R.attr.colorSurfaceDim),
    Surface(R.attr.colorSurface),
    SurfaceBright(R.attr.colorSurfaceBright),
    SurfaceContainerLowest(R.attr.colorSurfaceContainerLowest),
    SurfaceContainerLow(R.attr.colorSurfaceContainerLow),
    SurfaceContainer(R.attr.colorSurfaceContainer),
    SurfaceContainerHigh(R.attr.colorSurfaceContainerHigh),
    SurfaceContainerHighest(R.attr.colorSurfaceContainerHighest),
    OnSurface(R.attr.colorOnSurface),
    OnSurfaceVariant(R.attr.colorOnSurfaceVariant),
    Outline(R.attr.colorOutline),
    OutlineVariant(R.attr.colorOutlineVariant),

    InverseSurface(R.attr.colorSurfaceInverse),
    InverseOnSurface(R.attr.colorOnSurfaceInverse),
    InversePrimary(R.attr.colorPrimaryInverse),
}
