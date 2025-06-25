package org.ticanalyse.projetdevie.presentation.common

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.mon_reseau.MonReseauViewModel
import org.ticanalyse.projetdevie.utils.Global.validateTextEntries
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModal(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    icon: AppSubIcon,
    index: Int
) {
    val viewModel= hiltViewModel<MonReseauViewModel>()
    val upsertSuccess by viewModel.upsertSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current
    val ttsManager = appTTSManager()
    val sttManager = appSTTManager()
    val nom = rememberSaveable { mutableStateOf ("") }
    val description = rememberSaveable { mutableStateOf ("") }
    val nom2 = rememberSaveable { mutableStateOf ("") }
    val description2 = rememberSaveable { mutableStateOf ("") }
    val onSubmit = remember { mutableStateOf(false) }

    LaunchedEffect(index, icon.category) {
        viewModel.getReseauInfo(index = index, category = icon.category) { info ->
            val data = info?.split("|")
            if (data != null && data.size == 4) {
                nom.value = data[0]
                description.value = data[1]
                nom2.value = data[2]
                description2.value = data[3]
            }
        }
    }

    LaunchedEffect(upsertSuccess, showBottomSheet) {
        if (upsertSuccess && showBottomSheet) {
            Toast.makeText(context, "Insertion réussie", Toast.LENGTH_SHORT).show()
            viewModel.resetUpsertSuccess()
            onDismiss()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.resetErrorMessage()
        }
    }

    if (showBottomSheet){
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxSize(),
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            ),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
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
                }

                item {
                    AppTextInput (
                        value = nom.value,
                        onValueChange = { nom.value = it },
                        label = stringResource(id = R.string.nom_prenom),
                        ttsManager=ttsManager,
                        sttManager=sttManager,
                        onSubmit=onSubmit.value
                    )
                }

                item {
                    AppInputFieldMultiLine (
                        value = description.value,
                        onValueChange = { description.value = it },
                        label = stringResource(id = R.string.commentaire),
                        ttsManager=ttsManager,
                        sttManager=sttManager,
                        onSubmit=onSubmit.value
                    )
                }

                item {
                    AppTextInput (
                        value = nom2.value,
                        onValueChange = { nom2.value = it },
                        label = stringResource(id = R.string.nom_prenom),
                        ttsManager=ttsManager,
                        sttManager=sttManager,
                        onSubmit=onSubmit.value
                    )
                }

                item {
                    AppInputFieldMultiLine (
                        value = description2.value,
                        onValueChange = { description2.value = it },
                        label = stringResource(id = R.string.commentaire),
                        ttsManager=ttsManager,
                        sttManager=sttManager,
                        onSubmit=onSubmit.value
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))

                    AppButton(
                        text = stringResource(id = R.string.valider),
                        onClick = {
                            if( validateTextEntries(nom.value,description.value,nom2.value,description2.value))
                                viewModel.upsertData(index=index, category = icon.category, nom = nom.value, description = description.value, nom2 = nom2.value, description2 = description2.value)
                            else onSubmit.value=true
                            Timber.tag("tag").d("onsubmit: $onSubmit ")
                        }
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}