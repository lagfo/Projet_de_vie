package org.ticanalyse.projetdevie.presentation.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.common.AppAgeInput
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppPhoneInput
import org.ticanalyse.projetdevie.presentation.common.AppSelection
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.AppTextInput
import org.ticanalyse.projetdevie.presentation.common.ProfileAvatar
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager


@Composable
fun RegisterScreen (
    onNavigate: (String) -> Unit,
    onEvent: (RegisterEvent) -> Unit
){

    val imageUri = rememberSaveable { mutableStateOf ("") }
    val ttsManager = appTTSManager()
    val sttManager = appSTTManager()
    val nom = rememberSaveable { mutableStateOf ("") }
    val prenom = rememberSaveable { mutableStateOf ("") }
    val genre = rememberSaveable { mutableStateOf ("") }
    val genres = listOf("Homme", "Femme")
    val age = rememberSaveable { mutableStateOf ("") }
    val numTel = rememberSaveable { mutableStateOf ("") }
    val onSubmit = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.primary_color).copy(alpha = 0.05f))
    ) {

        Image(
            painter = painterResource(id = R.drawable.bg_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.1f
        )
        
        Box {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.4f)
                    .clip(RoundedCornerShape(bottomStartPercent = 50, bottomEndPercent = 50))
                ,
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


            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp, top = 25.dp)) {
                Box(modifier = Modifier
                    .weight(.9f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card (
                            modifier = Modifier
                                .fillMaxSize(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                            colors = CardDefaults.cardColors(Color.White),
                            shape = RoundedCornerShape(35.dp),
                            border = BorderStroke(3.dp, color = colorResource(R.color.secondary_color))
                        ){

                            Column (modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 5.dp, end = 5.dp)
                                .padding(start = 5.dp, end = 5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally

                            ){
                                Spacer(modifier = Modifier.height(15.dp))
                                AppText(stringResource(R.string.register_title),ttsManager=ttsManager, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(15.dp))
                                LazyColumn {
                                    item {
                                        Column {
                                            AppTextInput (
                                                value = nom.value,
                                                onValueChange = { nom.value = it },
                                                label = stringResource(id = R.string.nom),
                                                ttsManager=ttsManager,
                                                sttManager=sttManager,
                                                onSubmit=onSubmit.value
                                            )
                                            AppTextInput (
                                                value = prenom.value,
                                                onValueChange = { prenom.value = it },
                                                label = stringResource(id = R.string.prenom),
                                                ttsManager=ttsManager,
                                                sttManager=sttManager,
                                                onSubmit=onSubmit.value
                                            )
                                            Row (
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceAround
                                            ) {

                                                Box (modifier= Modifier.fillMaxWidth().weight(0.5f).padding(end = 2.dp)){
                                                    AppSelection(
                                                        value = genre.value,
                                                        onValueChange = { genre.value = it },
                                                        label = stringResource(id = R.string.genre),
                                                        options = genres,
                                                        onReadClick = { ttsManager.speak(genre.value) },
                                                        onSubmit=onSubmit.value
                                                    )
                                                }
                                                Box (modifier= Modifier.fillMaxWidth().weight(0.45f).padding(start = 2.dp)){
                                                    AppAgeInput(
                                                        value = age.value,
                                                        onValueChange = { age.value = it },
                                                        label = stringResource(id = R.string.age),
                                                        ttsManager = ttsManager,
                                                        sttManager = sttManager,
                                                        onSubmit=onSubmit.value
                                                    )
                                                }

                                            }
                                            AppPhoneInput(
                                                value = numTel.value,
                                                onValueChange = { numTel.value = it },
                                                label = stringResource(id = R.string.num_tel),
                                                ttsManager = ttsManager,
                                                sttManager = sttManager,
                                                onSubmit=onSubmit.value
                                            )

                                            Column(
                                                modifier = Modifier.fillMaxSize(),
                                                verticalArrangement = Arrangement.Bottom,
                                                horizontalAlignment = Alignment.CenterHorizontally

                                            ) {
                                                Spacer(modifier = Modifier.height(5.dp))

                                                ProfileAvatar (imageUri)

                                                Spacer(modifier = Modifier.height(5.dp))

                                                AppButton( text = stringResource(id=R.string.register_btn_title),
                                                    onClick = {
                                                        //onEvent(RegisterEvent.SaveAppEntry)
                                                        //onNavigate(Route.SplashScreen.route)
                                                        onSubmit.value=true
                                                    }
                                                )
                                                Spacer(modifier = Modifier.height(25.dp))

                                            }

                                        }
                                    }

                                }

                            }

                        }
                    }
                }
                Box (
                    modifier = Modifier.weight(.1f),
                    contentAlignment = Alignment.BottomEnd
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

}



