package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
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
                .fillMaxSize()
                .padding(start = 5.dp, end = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                Spacer(modifier = Modifier.height(15.dp))
                AppText(text = formTitle,ttsManager=ttsManager, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(5.dp))
                ProfileAvatar (imageUri)
                Spacer(modifier = Modifier.height(5.dp))
                val listState = rememberLazyListState()

                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .padding(1.dp)
                                .fillMaxSize(),
                            state = listState
                        ) {
                            item {
                                AppTextInput (
                                    value = nom.value,
                                    onValueChange = { nom.value = it },
                                    label = stringResource(id = R.string.nom),
                                    ttsManager=ttsManager,
                                    sttManager=sttManager,
                                    onSubmit=onSubmit.value
                                )
                            }
                            item {
                                AppTextInput (
                                    value = prenom.value,
                                    onValueChange = { prenom.value = it },
                                    label = stringResource(id = R.string.prenom),
                                    ttsManager=ttsManager,
                                    sttManager=sttManager,
                                    onSubmit=onSubmit.value
                                )
                            }
                            item {
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

                            }
                            item {
                                AppPhoneInput(
                                    value = numTel.value,
                                    onValueChange = { numTel.value = it },
                                    label = stringResource(id = R.string.num_tel),
                                    ttsManager = ttsManager,
                                    sttManager = sttManager,
                                    onSubmit=onSubmit.value
                                )

                            }
                            item {
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

                    Box(
                        modifier = Modifier
                            .width(15.dp)
                            .fillMaxHeight()
                            .padding(vertical = 25.dp)
                    ) {
                        val infiniteTransition = rememberInfiniteTransition()
                        val scale by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 2.5f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(800, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            )
                        )

                        if (listState.canScrollForward) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxSize()
                                    .background(
                                        color = colorResource(id=R.color.primary_color),
                                        shape = CircleShape
                                    )
                                    .scale(scale)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Scroll down",
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter),
                                    tint = Color.White
                                )
                            }
                        }
                        if (listState.canScrollBackward) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .fillMaxSize()
                                    .background(
                                        color = colorResource(id=R.color.primary_color),
                                        shape = CircleShape
                                    )
                                    .scale(scale)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Scroll up",
                                    modifier = Modifier
                                        .align(Alignment.TopCenter),
                                    tint = Color.White
                                )
                            }
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