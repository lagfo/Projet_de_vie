package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun UserInfoDialog(
    onDismiss: () -> Unit,
    onConfirm: (nom: String, prenom: String, telephone: String) -> Unit
) {
    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

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
                    singleLine = true
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
                    singleLine = true
                )

                OutlinedTextField(
                    value = telephone,
                    onValueChange = { telephone = it },
                    label = { Text("Numéro de téléphone") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
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
                onClick = {onDismiss}
            )

        }
    )
}