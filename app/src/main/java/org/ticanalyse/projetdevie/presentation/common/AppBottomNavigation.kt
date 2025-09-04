package org.ticanalyse.projetdevie.presentation.common

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme
import org.ticanalyse.projetdevie.utils.Dimens.BottomNavHeight
import org.ticanalyse.projetdevie.utils.Dimens.SmallIconSize1

@SuppressLint("UseKtx")
@Composable
fun AppBottomNavigation (
    items: List<BottomNavigationItem>,
    selectedItem: Int,
    onItemClick: (Int) -> Unit
){
    val context =LocalContext.current
    Column {

        Row( modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_helvetas),
                contentDescription = "Cliquez pour visiter le site",

                )
            Image(
                painter = painterResource(id = R.drawable.logo_tica),
                contentDescription = "Cliquez pour visiter le site",
                modifier = Modifier
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, "https://ticanalyse.org".toUri())
                        startActivity(context, intent, null)
                    }
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(BottomNavHeight),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            items.forEachIndexed { index, item ->

                Box(modifier = Modifier
                    .fillMaxSize()
                    .weight(0.5f)
                    .clickable { onItemClick(index) }
                    .background(if (selectedItem == index) Color.White else colorResource(id= R.color.primary_color))
                    .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier
                        .height(SmallIconSize1)
                        .clip(CircleShape)
                        .aspectRatio(1f)
                        .background(color = colorResource(id= R.color.primary_color))
                        .border(
                            BorderStroke(
                                width=1.dp,
                                color = Color.White
                            ),
                            shape = CircleShape
                        )
                        .padding(2.dp)
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = item.icon,
                            contentDescription = item.description,
                            tint = if (selectedItem == index) Color.White else Color.LightGray
                        )
                    }

                }
            }

        }

    }

}

data class BottomNavigationItem(val icon: ImageVector, val description:String)

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NewsBottomNavigationPreview() {
    ProjetDeVieTheme(dynamicColor = false) {
        val items = listOf(
            BottomNavigationItem(icon = Icons.Filled.Home, description = "Accueil"),
        )
        AppBottomNavigation(items=items,selectedItem = 0, onItemClick = {})
    }
}