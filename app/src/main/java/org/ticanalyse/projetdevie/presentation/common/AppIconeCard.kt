package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.navigation.NavController
import org.ticanalyse.projetdevie.utils.Dimens.ExtraSmallPadding2
import org.ticanalyse.projetdevie.utils.Dimens.IconSize

@Composable
fun AppCategoryIconCard(
    icon: AppIcon,
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit
){
    Column (
        modifier = modifier.clickable { onNavigate()  },
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Surface(
            modifier = Modifier.size(IconSize),
            shape = MaterialTheme.shapes.medium,
            color = Color.White,
            border = BorderStroke(2.dp, colorResource(icon.strokeColor))
        ) {
            Image(
                painter = painterResource(id=icon.paint),
                contentDescription =stringResource(id=icon.txt),
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
fun AppSubCategoryIconCard(
    icon: AppSubIcon
)
{
    Surface(
        modifier = Modifier.size(IconSize).padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(2.dp, colorResource(icon.strokeColor)),
        onClick = {  }
    ) {
        Box(contentAlignment = Alignment.BottomCenter) {
            Image(
                modifier = Modifier.fillMaxSize(),
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
                .padding(top = 5.dp, bottom = 1.dp, start = 1.dp, end = 1.dp),
                contentAlignment = Alignment.Center
            ) {
                AppIconText(
                    text = stringResource(id=icon.txt),
                    fontSize = 10.sp,
                    color = Color.White
                )

            }

        }

    }

}

