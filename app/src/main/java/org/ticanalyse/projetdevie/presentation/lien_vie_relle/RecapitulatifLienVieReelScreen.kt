package org.ticanalyse.projetdevie.presentation.lien_vie_relle

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.presentation.bilan_competance.generatePdf
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppInputFieldMultiLine
import org.ticanalyse.projetdevie.presentation.common.AppShape
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.UserInfoDialog
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.ligne_de_vie.LigneDeVieViewModel
import org.ticanalyse.projetdevie.ui.theme.Roboto
import java.time.LocalDate

@Composable
fun RecapitulatifLienVieReelScreen(
    modifier: Modifier = Modifier,
    onNavigate:()->Unit,
    onNavigatePdf:()->Unit
) {

    val ttsManager = appTTSManager()
    val sttManager = appSTTManager()
    var reponse1 by remember { mutableStateOf("") }
    var reponse2 by remember { mutableStateOf("") }
    var reponse3 by remember { mutableStateOf("") }
    val onSubmit = rememberSaveable { mutableStateOf (false) }
    val viewModel= hiltViewModel<LienVieReelViewModel>()
    val element by viewModel.allElement.collectAsStateWithLifecycle()
    var showUserDialog by remember { mutableStateOf(false) }
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(element) {
        if(element.isNotEmpty()){
            reponse1=element.first().firstResponse
            reponse2=element.first().secondResponse
            reponse3=element.first().thirdResponse
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            AppText(
                text = "Récapitulatifs",
                fontFamily = Roboto,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Normal,
                color = colorResource(R.color.primary_color),
                fontSize = 21.sp,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                ttsManager = ttsManager,
                isTextAlignCenter = true
            )

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
                        Spacer(modifier = Modifier.height(25.dp))
                        LazyColumn {
                            item {

                                Box(
                                    modifier= Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(10.dp,0.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally

                                    ) {
                                        AppText(
                                            text = "Qu'est-ce que j'ai actuellement?(Biens, Moyen Matériels, équipement, Argent)",
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
                                Box(
                                    modifier= Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(10.dp,0.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally

                                    ) {
                                        AppText(
                                            text = "Qu'est-ce qui me manque?(Biens, Moyen Matériels, équipement, Argent)",
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
                                Box(
                                    modifier= Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(10.dp,0.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally

                                    ) {
                                        AppText(
                                            text = "Ou puis-je trouver de l'aide ? Quelles personnes peuvent t'aider ?",
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

                            }
                        }

                    }

                }

            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ){
                AppButton("Planification du projet") {
                    viewModel.addLienDeVieReelLine(
                        firstResponse = reponse1,
                        secondResponse = reponse2,
                        thirdResponse = reponse3, creationDate = LocalDate.now().toString())
                    onNavigate()
                }

                AppButton("Generer Pdf") {
                    showUserDialog = true
                }
            }
        }

    }

    // Dialog pour saisir les informations utilisateur
    if (showUserDialog) {
        UserInfoDialog(
            onDismiss = { showUserDialog = false },
            onConfirm = { nom, prenom, telephone ->
                scope.launch {
                    // Sauvegarder l'utilisateur
                    val newUser = User(nom = nom, prenom = prenom, numTel = telephone)
                    viewModel.setCurrentUser(newUser)
                    onNavigatePdf()
                    showUserDialog = false
                    // Générer le PDF avec les nouvelles informations
                }
            }
        )
    }

}

@Composable
@Preview(showBackground = true)
fun RecapitulatifLienVieReelScreenPreview(modifier: Modifier = Modifier) {
    RecapitulatifLienVieReelScreen(onNavigate = {}, onNavigatePdf = {})
}