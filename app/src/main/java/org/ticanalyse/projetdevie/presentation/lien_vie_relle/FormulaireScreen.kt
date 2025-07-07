package org.ticanalyse.projetdevie.presentation.lien_vie_relle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppInputFieldMultiLine
import org.ticanalyse.projetdevie.presentation.common.AppShape
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.ui.theme.Roboto

@Composable
fun FormulaireScreen(modifier: Modifier = Modifier,onNavigate:()->Unit) {

    val ttsManager = appTTSManager()
    val sttManager = appSTTManager()
    var reponse1 by remember { mutableStateOf("") }
    var reponse2 by remember { mutableStateOf("") }
    var reponse3 by remember { mutableStateOf("") }
    val onSubmit = rememberSaveable { mutableStateOf (false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                text ="Identifie tes ressources et tes manques pour la réalisation de ton projet" ,
                maxLines = 5,
                textAlign = TextAlign.Center,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,

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
                                        Text(
                                            text ="Qu'est-ce que j'ai actuellement?(Biens, Moyen Matériels, équipement, Argent)",
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
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
                                        Text(
                                            modifier= Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                            text ="Qu'est-ce qui me manque?(Biens, Moyen Matériels, équipement, Argent)",
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
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
                                        Text(
                                            text ="Ou puis-je trouver de l'aide ? Quelles personnes peuvent t'aider ?",
                                            color = Color.White
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

            AppButton("Voir récapitulatif") {
                onNavigate()
            }
        }

    }

}

@Composable
@Preview(showBackground = true)
fun FormulaireScreenPreview(modifier: Modifier = Modifier) {
    FormulaireScreen(onNavigate = {})
}