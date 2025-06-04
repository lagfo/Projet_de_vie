package org.ticanalyse.projetdevie.presentation.common

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.ticanalyse.projetdevie.R

data class AppIcon(
    val route: Any,
    @StringRes val txt: Int,
    @ColorRes val strokeColor: Int,
    @DrawableRes val paint: Int,
)

val monReseauCategorie = listOf(
    AppIcon(
        route = "",
        txt = R.string.acteur_familiaux_sociaux,
        strokeColor = R.color.thirty_color,
        paint = R.drawable.acteur_familiaux_sociaux
    ),
    AppIcon(
        route = "",
        txt = R.string.acteur_educatif,
        strokeColor = R.color.primary_color,
        paint = R.drawable.acteur_educatif
    ),
    AppIcon(
        route = "",
        txt = R.string.acteur_professionnel,
        strokeColor = R.color.secondary_color,
        paint = R.drawable.acteur_familiaux_sociaux
    ),
    AppIcon(
        route = "",
        txt = R.string.acteur_institutionnel,
        strokeColor = R.color.fourty_color,
        paint = R.drawable.acteur_institutionnel
    ),

)

