package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme


@Composable
fun AppCategoryGrid(
    icons: List<AppIcon>,
    column:Int
) {

    val navController = rememberNavController()

    LazyVerticalGrid(
        columns = GridCells.Fixed(column),

    ) {
        items(icons){
            icon ->
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

@Composable
fun AppSubCategoryGrid(
    icons: List<AppIcon>,
    column:Int
) {

    val navController = rememberNavController()

    LazyVerticalGrid(
        columns = GridCells.Fixed(column),

        ) {
        items(icons){
                icon ->
            AppSubCategoryIconCard(
                icon = icon,
                modifier = Modifier.padding(15.dp),
                onNavigate = {
                    navController.navigate(icon.route)
                }
            )
        }

    }

}

@Composable
@Preview(showBackground = true)
fun AppGridPreview(){
    ProjetDeVieTheme {
        AppSubCategoryGrid(icons = monReseauCategorie, column = 2)

    }
}