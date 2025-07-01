package org.ticanalyse.projetdevie.presentation.profile

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppForm
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.utils.Global.validateNumber
import org.ticanalyse.projetdevie.utils.Global.validateTextEntries

@Composable
fun ProfileScreen(onSubmitClick: (User) -> Unit) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val currentUser = viewModel.currentUser.collectAsStateWithLifecycle()

    val imageUri = rememberSaveable { mutableStateOf(currentUser.value?.avatarUri ?: "") }
    val ttsManager = appTTSManager()
    val sttManager = appSTTManager()
    val nom = rememberSaveable { mutableStateOf (currentUser.value?.nom ?: "") }
    val prenom = rememberSaveable { mutableStateOf (currentUser.value?.prenom ?: "") }
    val genre = rememberSaveable { mutableStateOf (currentUser.value?.genre ?: "") }
    val email = rememberSaveable { mutableStateOf (currentUser.value?.email ?: "") }
    val genres = listOf("Homme", "Femme")
    val dateNaissance = rememberSaveable { mutableStateOf (currentUser.value?.dateNaissance ?: "") }
    val numTel = rememberSaveable { mutableStateOf (currentUser.value?.numTel ?: "") }
    val onSubmit = rememberSaveable { mutableStateOf(false) }


    Box{

        Image(
            painter = painterResource(id = R.drawable.bg_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

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


        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)) {

            Box(modifier = Modifier.weight(.9f)) {

                AppForm(
                    imageUri = imageUri,
                    ttsManager = ttsManager,
                    sttManager = sttManager,
                    nom = nom,
                    prenom = prenom,
                    genre = genre,
                    genres = genres,
                    dateNaissance = dateNaissance,
                    numTel = numTel,
                    email = email,
                    onSubmit = onSubmit,
                    formTitle = stringResource(id=R.string.profile_title)
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
                        if(validateTextEntries(nom.value,prenom.value,genre.value) and validateNumber(numTel.value)){
                            val user = User(
                                nom = nom.value,
                                prenom = prenom.value,
                                genre = genre.value,
                                dateNaissance = dateNaissance.value,
                                numTel = numTel.value,
                                avatarUri = imageUri.value,
                                email = email.value
                            )

                            onSubmitClick(user)
                        }
                    }

                )
                Spacer(modifier = Modifier.height(5.dp))
            }

        }

    }



}