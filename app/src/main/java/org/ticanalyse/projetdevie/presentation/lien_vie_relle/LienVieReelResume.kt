package org.ticanalyse.projetdevie.presentation.lien_vie_relle

import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.app_navigator.AppNavigationViewModel
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.PdfUtil.createLienVieReelPdf
import org.ticanalyse.projetdevie.utils.TextToSpeechManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.text.ifEmpty

@Composable
fun LienVieReelResume(modifier: Modifier = Modifier, onNavigate: () -> Unit) {

    val ttsManager = appTTSManager()

    val lienVieReelle = hiltViewModel<LienVieReelViewModel>()
    val viewModel = hiltViewModel<AppNavigationViewModel>()

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val painter = rememberAsyncImagePainter (currentUser?.avatarUri?.ifEmpty{R.drawable.avatar})

    val listLienVieReelle = lienVieReelle.allElement.collectAsStateWithLifecycle()
    val context = LocalContext.current


    LazyColumn (
        modifier = modifier.fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row (
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(MediumPadding1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color =colorResource(R.color.secondary_color),
                            shape = CircleShape
                        ),
                    painter = painter,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Profil image"
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Nom et prenoms: ${currentUser?.nom} ${currentUser?.prenom}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Sexe: ${currentUser?.genre}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Date de naissance: ${currentUser?.dateNaissance}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Numero de telephone: ${currentUser?.numTel}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    currentUser?.email?.let {
                        Text(
                            text = "Email: ${currentUser?.email?.ifEmpty { "Non renseigné" }}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            LienVieReelComponent(
                question = "Qu'est ce que j'ai actuellement ?",
                response = if (listLienVieReelle.value.isNotEmpty()) {
                    listLienVieReelle.value.first().firstResponse
                } else {
                    "Aucune reponse renseignée"
                },
                ttsManager = ttsManager
            )
        }
        item {
            LienVieReelComponent(
                question = "Qu'est ce qui me manque ?",
                response = if (listLienVieReelle.value.isNotEmpty()) {
                    listLienVieReelle.value.first().secondResponse
                } else {
                    "Aucune reponse renseignée"
                },
                ttsManager = ttsManager
            )
        }
        item {
            LienVieReelComponent(
                question = "Où puis-je trouver de l'aide ? Quelles personnes peuvent t'aider ?",
                response = if (listLienVieReelle.value.isNotEmpty()) {
                    listLienVieReelle.value.first().thirdResponse
                } else {
                    "Aucune reponse renseignée"
                },
                ttsManager = ttsManager
            )
        }

        item {
            AppButton("Telecharger pdf") {
                createLienVieReelPdf(
                    context = context,
                    user = currentUser!!,
                    listQuestionsLienVieReel = listOf(
                        Pair("Qu'est ce que j'ai actuellement ?", if (listLienVieReelle.value.isNotEmpty()) {
                            listLienVieReelle.value.first().firstResponse
                        } else {
                            "Aucune reponse renseignée"
                        }
                        ),
                        Pair("Qu'est ce qui me manque ?", if (listLienVieReelle.value.isNotEmpty()) {
                            listLienVieReelle.value.first().secondResponse
                        } else {
                            "Aucune reponse renseignée"
                        }),
                        Pair("Où puis-je trouver de l'aide ? Quelles personnes peuvent t'aider ?", if (listLienVieReelle.value.isNotEmpty()) {
                            listLienVieReelle.value.first().thirdResponse
                        } else {
                            "Aucune reponse renseignée"
                        })
                    ),
                ) {
                    viewModel.setResumeUri(
                        "${context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}" +
                                "/lien_vie_reel_${currentUser!!.nom}_${currentUser!!.prenom}_${
                                    LocalDateTime.now().format(
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
                                    )
                                }.pdf",
                        "lienVieReel"
                    )
                    onNavigate()
                }
            }
        }

    }

}

@Composable
fun LienVieReelComponent(
    question: String = "Questions",
    response: String = "Description",
    ttsManager: TextToSpeechManager?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        AppText(
            text = question,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            ttsManager = ttsManager
        )
        AppText(
            text = response,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 6.dp),
            ttsManager = ttsManager,
        )
    }

}
