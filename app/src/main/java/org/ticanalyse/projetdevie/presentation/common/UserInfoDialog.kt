package org.ticanalyse.projetdevie.presentation.common

import android.app.Activity
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.ticanalyse.projetdevie.R

@Composable
fun UserInfoDialog(
    onDismiss: () -> Unit,
    onConfirm: (nom: String, prenom: String, telephone: String) -> Unit,
    onSpeechResult: ((String) -> String)? = null
) {
    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var activeField by remember { mutableStateOf("") }

    val ttsManager = appTTSManager()
    val sttManager = appSTTManager()

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                val processed = onSpeechResult?.invoke(matches[0]) ?: matches[0]
                when (activeField) {
                    "nom" -> nom = processed
                    "prenom" -> prenom = processed
                    "telephone" -> telephone = processed
                }
                showError = false
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Informations utilisateur",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Veuillez renseigner vos informations pour générer le PDF :",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = nom,
                    onValueChange = {
                        nom = it
                        showError = false
                    },
                    label = { Text("Nom *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError && nom.isBlank(),
                    singleLine = true,
                    leadingIcon = {
                        IconButton(onClick = { ttsManager.speak(nom) }) {
                            Icon(
                                painter = painterResource(R.drawable.outline_ear_sound_24),
                                contentDescription = "Lire le texte"
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            activeField = "nom"
                            sttManager.startSpeechToText(speechLauncher)
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.outline_mic_24),
                                contentDescription = "Saisie vocale"
                            )
                        }
                    }
                )

                OutlinedTextField(
                    value = prenom,
                    onValueChange = {
                        prenom = it
                        showError = false
                    },
                    label = { Text("Prénom *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError && prenom.isBlank(),
                    singleLine = true,
                    leadingIcon = {
                        IconButton(onClick = { ttsManager.speak(prenom) }) {
                            Icon(
                                painter = painterResource(R.drawable.outline_ear_sound_24),
                                contentDescription = "Lire le texte"
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            activeField = "prenom"
                            sttManager.startSpeechToText(speechLauncher)
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.outline_mic_24),
                                contentDescription = "Saisie vocale"
                            )
                        }
                    }
                )

                OutlinedTextField(
                    value = telephone,
                    onValueChange = { telephone = it },
                    label = { Text("Numéro de téléphone") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    leadingIcon = {
                        IconButton(onClick = { ttsManager.speak(telephone) }) {
                            Icon(
                                painter = painterResource(R.drawable.outline_ear_sound_24),
                                contentDescription = "Lire le texte"
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            activeField = "telephone"
                            sttManager.startSpeechToText(speechLauncher)
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.outline_mic_24),
                                contentDescription = "Saisie vocale"
                            )
                        }
                    }
                )

                if (showError) {
                    Text(
                        text = "Le nom et le prénom sont obligatoires",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            AppButton(
                text = "Confirmer",
                onClick = {
                    if (nom.isNotBlank() && prenom.isNotBlank()) {
                        onConfirm(nom.trim(), prenom.trim(), telephone.trim())
                    } else {
                        showError = true
                    }
                }
            )
        },
        dismissButton = {
            AppTextButton(
                text = "Annuler",
                onClick = onDismiss
            )
        }
    )
}