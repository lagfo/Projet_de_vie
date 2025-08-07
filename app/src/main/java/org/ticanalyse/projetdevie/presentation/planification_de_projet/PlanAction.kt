package org.ticanalyse.projetdevie.presentation.planification_de_projet

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.PlanAction
import org.ticanalyse.projetdevie.presentation.common.AppInputFieldMultiLine
import org.ticanalyse.projetdevie.presentation.common.AppShape
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3
import timber.log.Timber
import java.time.LocalDate

@Composable
fun PlanActionScreen(modifier: Modifier = Modifier) {

    val ttsManager = appTTSManager()
    val sttManager = appSTTManager()
    var reponse1 by rememberSaveable{ mutableStateOf("") }
    var reponse2 by rememberSaveable{ mutableStateOf("") }
    var reponse3 by rememberSaveable{ mutableStateOf("") }
    var reponse4 by rememberSaveable{ mutableStateOf("") }
    var descriptionActivite by rememberSaveable{ mutableStateOf("") }
    var isResponseValide by remember { mutableStateOf(false) }
    var isClicked by remember { mutableStateOf(false) }
    val onSubmit = rememberSaveable { mutableStateOf (false) }
    val planAction = PlanAction()
    val context = LocalContext.current
    val viewModel= hiltViewModel<PlanificationViewModel>()
    val statusPlanAction by viewModel.upsertSuccess.collectAsStateWithLifecycle()

    // Reset status when the screen is disposed (user leaves the screen)
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetUpsertStatus()
        }
    }

    LaunchedEffect(statusPlanAction){
        if(statusPlanAction){
            Toast.makeText(context, "Planification enregistrée", Toast.LENGTH_SHORT).show()
            viewModel.resetUpsertStatus()
        }
    }

    LaunchedEffect(isClicked,context) {
        if(!isResponseValide && isClicked){
            Toast.makeText(context, "Vous devriez obligatoirement repondre à toutes les questions", Toast.LENGTH_SHORT).show()
            isClicked=false
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha = 0.07f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = MediumPadding3, bottom = MediumPadding1, start = 5.dp, end = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box {
                AppShape(
                    cornerRadius = 45.dp,
                    arcHeight = 0.dp,
                    modifier = Modifier.fillMaxHeight(.85f),
                    bottomCornerRadius = 45.dp
                )

                Box(
                    modifier = Modifier.fillMaxHeight(.80f),
                    contentAlignment = Alignment.Center
                ) {
                    Column{
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
                                    state = listState) {
                                    item {
                                        Column(
                                            modifier = Modifier.padding(10.dp,0.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally

                                        ) {
                                            AppText(
                                                text = "Activités",
                                                fontFamily = Roboto,
                                                fontWeight = FontWeight.Black,
                                                fontStyle = FontStyle.Normal,
                                                color =Color.White,
                                                fontSize = 12.sp,
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                                ttsManager = ttsManager,
                                                isDefineMaxLine = true
                                            )
                                            AppInputFieldMultiLine(
                                                value =reponse1,
                                                onValueChange = {
                                                    reponse1=it
                                                },
                                                label ="",
                                                ttsManager =ttsManager,
                                                sttManager =sttManager,
                                                onSubmit=onSubmit.value
                                            )

                                        }
                                    }
                                    item {
                                        Column(
                                            modifier = Modifier.padding(10.dp,0.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally

                                        ) {
                                            AppText(
                                                text = "Description de l'activité",
                                                fontFamily = Roboto,
                                                fontWeight = FontWeight.Black,
                                                fontStyle = FontStyle.Normal,
                                                color =Color.White,
                                                fontSize = 12.sp,
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                                ttsManager = ttsManager,
                                                isDefineMaxLine = true
                                            )
                                            AppInputFieldMultiLine(
                                                value =descriptionActivite,
                                                onValueChange = {
                                                    descriptionActivite=it
                                                },
                                                label ="",
                                                ttsManager =ttsManager,
                                                sttManager =sttManager,
                                                onSubmit=onSubmit.value
                                            )

                                        }
                                    }
                                    item {
                                        Column(
                                            modifier = Modifier.padding(10.dp,0.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally

                                        ) {
                                            AppText(
                                                text = "Qui participe au projet ?",
                                                fontFamily = Roboto,
                                                fontWeight = FontWeight.Black,
                                                fontStyle = FontStyle.Normal,
                                                color =Color.White,
                                                fontSize = 12.sp,
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                                ttsManager = ttsManager,
                                                isDefineMaxLine = true
                                            )
                                            AppInputFieldMultiLine(
                                                value =reponse2,
                                                onValueChange = {
                                                    reponse2=it
                                                },
                                                label ="",
                                                ttsManager =ttsManager,
                                                sttManager =sttManager
                                            )

                                        }
                                    }
                                    item {
                                        Column(
                                            modifier = Modifier.padding(10.dp,0.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally

                                        ) {
                                            AppText(
                                                text = "Qui subventionne le projet ?",
                                                fontFamily = Roboto,
                                                fontWeight = FontWeight.Black,
                                                fontStyle = FontStyle.Normal,
                                                color =Color.White,
                                                fontSize = 12.sp,
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                                ttsManager = ttsManager
                                            )
                                            AppInputFieldMultiLine(
                                                value =reponse3,
                                                onValueChange = {
                                                    reponse3=it
                                                },
                                                label ="",
                                                ttsManager =ttsManager,
                                                sttManager =sttManager
                                            )

                                        }

                                    }
                                    item {

                                        Column(
                                            modifier = Modifier.padding(10.dp,0.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally

                                        ) {
                                            AppText(
                                                text = "Quand ?",
                                                fontFamily = Roboto,
                                                fontWeight = FontWeight.Black,
                                                fontStyle = FontStyle.Normal,
                                                color =Color.White,
                                                fontSize = 12.sp,
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                                ttsManager = ttsManager
                                            )
                                            AppInputFieldMultiLine(
                                                value =reponse4,
                                                onValueChange = {
                                                    reponse4=it
                                                },
                                                label ="",
                                                ttsManager =ttsManager,
                                                sttManager =sttManager
                                            )

                                        }

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
                                    initialValue = 0.5f,
                                    targetValue = 1f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(800, easing = FastOutSlowInEasing),
                                        repeatMode = RepeatMode.Reverse
                                    )
                                )

                                if (listState.canScrollForward ) {
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
                                if ( listState.canScrollBackward) {
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

                    FloatingActionButton(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.BottomEnd)
                            .offset(
                                ((-10).dp),
                                (80).dp
                            ),
                        onClick = {
                            isClicked=true
                            if(reponse1.isNotEmpty()&&reponse1.isNotBlank()&&reponse2.isNotEmpty()&&reponse2.isNotBlank()&&reponse3.isNotEmpty()&&reponse3.isNotBlank()&&reponse4.isNotEmpty()&&reponse4.isNotBlank()&&descriptionActivite.isNotEmpty()&&descriptionActivite.isNotBlank()){
                                isResponseValide=true
                                //Enregsitrer ligne
                                planAction.activite=reponse1
                                planAction.activiteDescription=descriptionActivite
                                planAction.acteur=reponse2
                                planAction.financement=reponse3
                                planAction.periode=reponse4
                                planAction.creationDate= LocalDate.now().toString()
                                viewModel.addPLanAction(planAction)
                                //Reset fields
                                reponse1=""
                                reponse2=""
                                reponse3=""
                                reponse4=""
                                descriptionActivite=""

                            }else{
                                isResponseValide=false
                            }
                        },
                        containerColor = Color.Black,
                        contentColor = Color.White,
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Ajouter",
                        )
                    }
                }

            }
        }

    }

}