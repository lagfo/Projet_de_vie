package org.ticanalyse.projetdevie.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.presentation.app_navigator.AppNavigationViewModel
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppProfileForm
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.utils.Global.validateNumber
import org.ticanalyse.projetdevie.utils.Global.validateTextEntries

@Composable
fun ProfileScreen(
    navController: NavController,
    sharedViewModel: AppNavigationViewModel? = null
) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val saveSuccess by viewModel.saveSuccess.collectAsStateWithLifecycle()

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
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            snackbarHostState.showSnackbar(
                message = "Profil mis à jour avec succès",
                duration = SnackbarDuration.Short
            )
            viewModel.resetSaveSuccess()
            sharedViewModel?.refreshCurrentUser()
        }
    }

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
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
                .zIndex(1f)
        ) { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
                containerColor = colorResource(R.color.primary_color),
                contentColor = Color.White,
                shape = RoundedCornerShape(8.dp)

            )
        }

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
            Box(modifier = Modifier.weight(.9f)) {

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

            }

            errorMessage?.let { message ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(16.dp),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppButton(
                    text = "Retour",
                    onClick = { navController.navigateUp() }
                )
                Spacer(modifier = Modifier.width(24.dp))
                AppButton(
                    text = stringResource(id = R.string.edit_btn),
                    enabled = !isLoading,
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
                            viewModel.onSubmit(user)
                        }else onSubmit.value=true
                    }
                )
            }
        }
    }
}
