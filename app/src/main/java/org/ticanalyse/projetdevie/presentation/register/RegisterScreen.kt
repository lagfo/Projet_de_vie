package org.ticanalyse.projetdevie.presentation.register

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppForm
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.nvgraph.Route
import org.ticanalyse.projetdevie.utils.Global.validateAge
import org.ticanalyse.projetdevie.utils.Global.validateNumer
import org.ticanalyse.projetdevie.utils.Global.validateTextEntries


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
    val onSubmit = rememberSaveable { mutableStateOf(false) }

    Box{

        Image(
            painter = painterResource(id = R.drawable.bg_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        //Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.35f)
                .clip(RoundedCornerShape(bottomStartPercent = 45, bottomEndPercent = 45))
            ,
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.register_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStartPercent = 45, bottomEndPercent = 45))
                    .background(colorResource(id = R.color.primary_color).copy(alpha = 0.5f))
            )
        }

        //Register

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, top = 25.dp)) {

            //Form
            Box(modifier = Modifier.weight(.9f)) {

                AppForm(
                    imageUri = imageUri,
                    ttsManager = ttsManager,
                    sttManager = sttManager,
                    nom = nom,
                    prenom = prenom,
                    genre = genre,
                    genres = genres,
                    age = age,
                    numTel = numTel,
                    onSubmit = onSubmit
                )

            }

            Column(
                modifier = Modifier.fillMaxWidth().weight(.1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                ) {
                AppButton(
                    text = stringResource(id = R.string.register_btn_title),
                    onClick = {
                        onSubmit.value = true
                        if(validateTextEntries(nom.value,prenom.value,genre.value) and validateAge(age.value) and validateNumer(numTel.value)){
                            onEvent(RegisterEvent.SaveAppEntry)
                            val user = User(
                                nom = nom.value,
                                prenom = prenom.value,
                                genre = genre.value,
                                age = age.value,
                                numTel = numTel.value,
                                avatarUri = imageUri.value
                            )
                            onEvent(RegisterEvent.UpsertUser(user))
                            onNavigate(Route.SplashScreen.route)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
            }

        }

    }

}



