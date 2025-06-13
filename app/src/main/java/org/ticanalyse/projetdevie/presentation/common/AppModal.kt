package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ticanalyse.projetdevie.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModal(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    icon: AppSubIcon,
    index: Int
) {

    if (showBottomSheet){
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box{

                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box {

                                AppIconCard(paintId = icon.paint, colorId = icon.strokeColor, txtId = icon.txt)
                            }
                            Spacer(modifier = Modifier.width(2.dp))
                            Box {
                                AppIconText(
                                    text = stringResource(id=icon.txt).trimStart(),
                                    fontSize = 15.sp,
                                    color = Color.Black,
                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight.ExtraBold,
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                        val ttsManager = appTTSManager()
                        val sttManager = appSTTManager()
                        val nom = rememberSaveable { mutableStateOf ("") }
                        val description = rememberSaveable { mutableStateOf ("") }
                        val onSubmit = rememberSaveable { mutableStateOf (false) }

                        AppTextInput (
                            value = nom.value,
                            onValueChange = { nom.value = it },
                            label = stringResource(id = R.string.nom_prenom),
                            ttsManager=ttsManager,
                            sttManager=sttManager,
                            onSubmit=onSubmit.value
                        )
                        AppInputFieldMultiLine (
                            value = description.value,
                            onValueChange = { description.value = it },
                            label = stringResource(id = R.string.commentaire),
                            ttsManager=ttsManager,
                            sttManager=sttManager,
                            onSubmit=onSubmit.value
                        )


                    }

                }

                Spacer(modifier = Modifier.height(10.dp))

                AppButton(
                    text = stringResource(id = R.string.valider),
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(15.dp))

            }





        }
    }

}