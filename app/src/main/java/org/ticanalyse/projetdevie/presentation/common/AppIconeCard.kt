package org.ticanalyse.projetdevie.presentation.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme
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
fun AppIconCard(
    @DrawableRes  paintId:Int,
    @ColorRes colorId: Int,
    @StringRes txtId: Int
){
    Surface(
        modifier = Modifier.size(100.dp),
        shape = MaterialTheme.shapes.medium,
        color = Color.White,
        border = BorderStroke(2.dp, colorResource(id = colorId))
    ) {
        Image(
            painter = painterResource(id=paintId),
            contentDescription =stringResource(id=txtId),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun AppSubCategoryIconCard(
    icon: AppSubIcon,
    index: Int
)
{
    var showBottomSheet by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.size(IconSize).padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(2.dp, colorResource(icon.strokeColor)),
        onClick = {
            showBottomSheet = true
        }
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

    AppModal(
        showBottomSheet = showBottomSheet,
        onDismiss = { showBottomSheet = false },
        icon = icon,
        index = index
    )

}

@Composable
fun AppSkillIconCard(
    icon: AppSkillCardIcon
) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
                ambientColor = colorResource(icon.strokeColor),
                spotColor = colorResource(icon.strokeColor))
            .background(Color.Green, CircleShape)
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "✓",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
    Surface(
        modifier = Modifier.size(IconSize).padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(2.dp, colorResource(icon.strokeColor)),
        onClick = {}
    ) {

        Box {

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id=icon.paint),
                contentDescription =null,
                contentScale = ContentScale.Crop,
            )

            Box(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
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
                val displayText = when (val txt = icon.txt) {
                    is Txt.Res -> stringResource(id = txt.id)
                    is Txt.Raw -> txt.text
                }
                AppIconText(
                    text = displayText,
                    fontSize = 10.sp,
                    color = Color.White
                )

            }

        }

    }

}


@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AppSkillIconCardPreview() {
    ProjetDeVieTheme(dynamicColor = false) {
        val item = AppSkillCardIcon(txt = Txt.Res(R.string.skill_agriculture), strokeColor = R.color.primary_color, paint = R.drawable.agriculture)
        AppSkillIconCard(icon  =item)
    }
}

