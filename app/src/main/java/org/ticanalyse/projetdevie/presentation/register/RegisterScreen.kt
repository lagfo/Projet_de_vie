package org.ticanalyse.projetdevie.presentation.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.common.AppInput
import org.ticanalyse.projetdevie.presentation.common.AppSelection
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme


@Composable
fun RegisterScreen (){
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.primary_color).copy(alpha = 0.05f))

    ){
        Image(
            painter = painterResource(id = R.drawable.bg_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.1f
        )
        Column (
            modifier = Modifier
            .fillMaxSize()
        ){
            ConstraintLayout (
                modifier = Modifier.fillMaxSize().weight(0.9f)
            ){
                val (topBanner,card)=createRefs()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.65f)
                        .clip(RoundedCornerShape(bottomStartPercent = 50, bottomEndPercent = 50))
                        .constrainAs(topBanner){
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    contentAlignment = Alignment.TopCenter
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.register_bg),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = 0.65f
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(bottomStartPercent = 50, bottomEndPercent = 50))
                            .background(colorResource(id = R.color.primary_color).copy(alpha = 0.5f))
                    )
                }

                Column (modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, top = 50.dp)
                    .constrainAs(card){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = painterResource(id = R.drawable.mini_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(100.dp)
                    )

                    Card (
                        modifier = Modifier
                            .fillMaxSize(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(35.dp),
                        border = BorderStroke(3.dp, color = colorResource(R.color.primary_color))
                    ){
                        Column (modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 5.dp, end = 5.dp)
                            .padding(start = 5.dp, end = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally

                        ){
                            Spacer(modifier = Modifier.height(15.dp))
                            val ttsManager = appTTSManager()
                            val sttManager = appSTTManager()
                            AppText(stringResource(R.string.register_title),ttsManager=ttsManager, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(15.dp))
                            var nom by remember { mutableStateOf("") }
                            var prenom by remember { mutableStateOf("") }
                            var genre by remember { mutableStateOf("") }
                            val genres = listOf("Homme", "Femme")

                            AppInput (
                                value = nom,
                                onValueChange = { nom = it },
                                label = "Nom",
                                ttsManager=ttsManager,
                                sttManager=sttManager
                            )
                            AppInput (
                                value = prenom,
                                onValueChange = { prenom = it },
                                label = "Prénom",
                                ttsManager=ttsManager,
                                sttManager=sttManager
                            )
                            AppSelection (
                                value = genre,
                                onValueChange = { genre = it },
                                label = "Genre",
                                options = genres
                            )

                        }

                    }

                }

            }

            Box(modifier = Modifier
                .fillMaxSize()
                .weight(0.1f),
                contentAlignment = Alignment.BottomCenter
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_helvetas),
                        contentDescription = "Logo Helvetas",
                        modifier = Modifier.fillMaxWidth(1f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.logo_tica),
                        contentDescription = "Logo Tica",
                        modifier = Modifier.fillMaxWidth(1f)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview(){
    ProjetDeVieTheme {
        RegisterScreen ()
    }
}
