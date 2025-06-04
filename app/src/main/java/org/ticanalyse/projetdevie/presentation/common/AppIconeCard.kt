package org.ticanalyse.projetdevie.presentation.common

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ticanalyse.projetdevie.utils.Dimens.ExtraSmallPadding2
import org.ticanalyse.projetdevie.utils.Dimens.IconSize

@Composable
fun AppCategoryIconCard(icon: Icon){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Surface(
            modifier = Modifier.size(IconSize),
            shape = MaterialTheme.shapes.medium,
            color = Color.White,                                      // ← solid background
            border = BorderStroke(2.dp, colorResource(icon.strokeColor))
        ) {
            Image(
                painter = painterResource(id=icon.paint),
                contentDescription =null,
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = Modifier.height(ExtraSmallPadding2))
        AppIconText(
            modifier = Modifier,
            text = stringResource(id=icon.txt),
            fontSize = 15.sp
        )

    }

}

@Composable
fun AppSubCategoryIconCard(icon: Icon){

    Surface(
        modifier = Modifier.size(IconSize),
        shape = MaterialTheme.shapes.medium,
        color = Color.White,                                      // ← solid background
        border = BorderStroke(2.dp, colorResource(icon.strokeColor))
    ) {
        Box(contentAlignment = Alignment.BottomCenter) {
            Image(
                painter = painterResource(id=icon.paint),
                contentDescription =null,
                contentScale = ContentScale.Crop,
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colorResource(id = icon.strokeColor).copy(alpha = 0.7f),
                            colorResource(id = icon.strokeColor).copy(alpha = 0.8f),
                            colorResource(id = icon.strokeColor).copy(alpha = 0.9f),
                            colorResource(id = icon.strokeColor),
                            colorResource(id = icon.strokeColor)
                        )
                    )
                )
                .padding(top = 5.dp,bottom = 1.dp),
                contentAlignment = Alignment.BottomCenter
            ) {

                AppIconText(
                    text = stringResource(id=icon.txt),
                    fontSize = 12.sp,
                    color = Color.White
                )

            }
            
        }

    }

}

data class Icon (
    @StringRes val txt: Int,
    @ColorRes val strokeColor: Int,
    @DrawableRes val paint: Int,
)