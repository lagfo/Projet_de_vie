package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.utils.SpeechToTextManager
import org.ticanalyse.projetdevie.utils.TextToSpeechManager

@Composable
fun AppForm(
    imageUri : MutableState<String>,
    ttsManager : TextToSpeechManager,
    sttManager : SpeechToTextManager,
    nom : MutableState<String>,
    prenom : MutableState<String>,
    genre : MutableState<String>,
    genres : List<String>,
    dateNaissance : MutableState<String>,
    numTel : MutableState<String>,
    email: MutableState<String>,
    onSubmit: MutableState<Boolean>,
    formTitle: String = stringResource(R.string.register_title)
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.mini_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth(.2f)
                .aspectRatio(1f)
        )
        Card (
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(Color.White),
            shape = RoundedCornerShape(35.dp),
            border = BorderStroke(3.dp, color = colorResource(R.color.secondary_color))
        ){

            Column (modifier = Modifier
                .padding(start = 5.dp, end = 5.dp)
                .padding(start = 5.dp, end = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                Spacer(modifier = Modifier.height(15.dp))
                AppText(text = formTitle,ttsManager=ttsManager, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(5.dp))
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

                                Box (modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f)
                                    .padding(end = 2.dp)){
                                    AppSelection(
                                        value = genre.value,
                                        onValueChange = { genre.value = it },
                                        label = stringResource(id = R.string.genre),
                                        options = genres,
                                        onReadClick = { ttsManager.speak(genre.value) },
                                        onSubmit=onSubmit.value
                                    )
                                }
                                Box (modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(0.45f)
                                    .padding(start = 2.dp)){
                                    AppBirthDateInput(
                                        value = dateNaissance.value,
                                        onValueChange = { dateNaissance.value = it },
                                        label = "Date",
//                                        ttsManager = ttsManager,
//                                        sttManager = sttManager,
                                        onSubmit = onSubmit.value
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
                            AppInputField(
                                value = email.value,
                                onValueChange = { email.value = it },
                                label = "email",
                                inputType = "email",
                                ttsManager = ttsManager,
                                sttManager = sttManager,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                onSubmit = onSubmit.value
                            )
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Spacer(modifier = Modifier.height(10.dp))

                                ProfileAvatar (imageUri)

                                Spacer(modifier = Modifier.height(10.dp))


                            }

                        }
                    }

                }

            }

        }
    }
}

@Composable
fun AppProfileForm(
    imageUri : MutableState<String>,
    ttsManager : TextToSpeechManager,
    sttManager : SpeechToTextManager,
    nom : MutableState<String>,
    prenom : MutableState<String>,
    genre : MutableState<String>,
    genres : List<String>,
    dateNaissance : MutableState<String>,
    numTel : MutableState<String>,
    email: MutableState<String>,
    onSubmit: MutableState<Boolean>,
    formTitle: String = stringResource(R.string.register_title)
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.mini_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth(.2f)
                .aspectRatio(1f)
        )
        Card (
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(Color.White),
            shape = RoundedCornerShape(35.dp),
            border = BorderStroke(3.dp, color = colorResource(R.color.secondary_color))
        ){

            Column (modifier = Modifier
                .padding(start = 5.dp, end = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                Spacer(modifier = Modifier.height(15.dp))
                AppText(text = formTitle,ttsManager=ttsManager, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(5.dp))
                ProfileAvatar (imageUri)
                Spacer(modifier = Modifier.height(5.dp))
                val listState = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier.padding(10.dp),
                    state = listState
                ) {
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

                                Box (modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f)
                                    .padding(end = 2.dp)){
                                    AppSelection(
                                        value = genre.value,
                                        onValueChange = { genre.value = it },
                                        label = stringResource(id = R.string.genre),
                                        options = genres,
                                        onReadClick = { ttsManager.speak(genre.value) },
                                        onSubmit=onSubmit.value
                                    )
                                }
                                Box (modifier= Modifier
                                    .fillMaxWidth()
                                    .weight(0.45f)
                                    .padding(start = 2.dp)){
                                    AppBirthDateInput(
                                        value = dateNaissance.value,
                                        onValueChange = { dateNaissance.value = it },
                                        label = "Date",
                                        onSubmit = onSubmit.value
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
                            AppInputField(
                                value = email.value,
                                onValueChange = { email.value = it },
                                label = "email",
                                inputType = "email",
                                ttsManager = ttsManager,
                                sttManager = sttManager,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                onSubmit = onSubmit.value
                            )

                        }

                    }

                }

            }

        }
    }
}

@Preview
@Composable
fun AppFormPreview(modifier: Modifier = Modifier) {

}