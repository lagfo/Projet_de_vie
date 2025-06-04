package org.ticanalyse.projetdevie.presentation.mon_reseau

import androidx.annotation.DrawableRes

data class ListeSousCategorie(
    val title: String,
    val route: String,
    @DrawableRes val image: Int
)
