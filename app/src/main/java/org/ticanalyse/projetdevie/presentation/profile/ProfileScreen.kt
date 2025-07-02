package org.ticanalyse.projetdevie.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppProfileForm
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.utils.Global.validateNumber
import org.ticanalyse.projetdevie.utils.Global.validateTextEntries

@Composable
fun ProfileScreen(
    navController: NavController,
    onSubmitClick: (User) -> Unit
) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val imageUri = remember { mutableStateOf("") }
    val ttsManager = appTTSManager()
    val sttManager = appSTTManager()
    val nom = remember { mutableStateOf("") }
    val prenom = remember { mutableStateOf("") }
    val genre = remember { mutableStateOf("") }
    val genres = listOf("Homme", "Femme")
    val dateNaissance = remember { mutableStateOf("") }
    val numTel = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val onSubmit = remember { mutableStateOf(false) }


    LaunchedEffect(currentUser) {
        currentUser?.let {
            imageUri.value = it.avatarUri
            nom.value = it.nom
            prenom.value = it.prenom
            genre.value = it.genre
            dateNaissance.value = it.dateNaissance
            numTel.value = it.numTel
            email.value = it.email
        }
    }

    Box {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {


                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    AppProfileForm(
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






                AppButton(
                    text = stringResource(id = R.string.register_btn_title),
                    onClick = {
                        if (validateTextEntries(nom.value, prenom.value, genre.value) && validateNumber(numTel.value)) {
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
            }
        }
    }
}
