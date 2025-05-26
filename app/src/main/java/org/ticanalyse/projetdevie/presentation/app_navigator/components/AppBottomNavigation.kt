package org.ticanalyse.projetdevie.presentation.app_navigator.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable

@Composable
fun AppBottomNavigation (
    items: List<BottomNavigationItem>,
    selectedItem: Int,
    onItemClick: (Int) -> Unit
) {

}

data class BottomNavigationItem(
    @DrawableRes val icon: Int,
    val text: String
)