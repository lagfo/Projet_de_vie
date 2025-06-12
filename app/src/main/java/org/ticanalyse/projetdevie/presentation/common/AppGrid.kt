package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun AppCategoryGrid(
    icons: List<AppIcon>,
    column:Int,
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(column)

    ) {
        items(icons){
            icon ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                AppCategoryIconCard(
                    icon = icon,
                    modifier = Modifier.padding(15.dp),
                    onNavigate = {
                        navController.navigate(icon.route)
                    }
                )
            }

        }

    }

}

@Composable
fun AppSubCategoryGrid(
    icons: List<AppSubIcon>,
    column:Int,
) {


    LazyVerticalGrid(
        columns = GridCells.Fixed(column),
        modifier = Modifier.fillMaxSize()
        ) {

        /*items(icons){
                icon ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AppSubCategoryIconCard(icon = icon)
            }
        }

         */


        val lastIndex = icons.lastIndex
        icons.forEachIndexed { index, icon ->
            if (index == lastIndex && icons.size % 2 != 0 && column > 1) {
                item(span = { GridItemSpan(column) }) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        AppSubCategoryIconCard(icon = icon)
                    }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        AppSubCategoryIconCard(icon = icon)
                    }

                }
            }
        }
    }
}
