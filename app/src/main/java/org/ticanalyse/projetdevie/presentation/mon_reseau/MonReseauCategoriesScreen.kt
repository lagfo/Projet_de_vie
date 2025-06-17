package org.ticanalyse.projetdevie.presentation.mon_reseau

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppCategoryGrid
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.common.monReseauCategories
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3

@Composable
fun MonReseauCategoriesScreen(navController:NavController,onNavigate: () -> Unit) {
    val ttsManager = appTTSManager()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha = 0.07f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = MediumPadding3, bottom = MediumPadding1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppText(
                        text = stringResource(id = R.string.mon_reseau_categories_title),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Normal,
                        color = colorResource(id = R.color.text),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        ttsManager = ttsManager
                    )
                    Spacer(modifier = Modifier.height(MediumPadding1))

                    AppCategoryGrid(icons = monReseauCategories, column = 2, navController = navController)
                }

            }

            AppButton(text = "Ligne de Vie", onClick = onNavigate )
        }

    }
}