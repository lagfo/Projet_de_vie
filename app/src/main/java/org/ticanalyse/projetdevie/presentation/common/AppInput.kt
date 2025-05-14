package org.ticanalyse.projetdevie.presentation.common

import android.app.DatePickerDialog
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme
import org.ticanalyse.projetdevie.utils.SpeechToTextManager
import org.ticanalyse.projetdevie.utils.TextToSpeechManager
import java.util.Calendar

@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    onReadClick: () -> Unit,
    onSpeechToTextClick: () -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
){



    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(50),
        leadingIcon = {
            IconButton(onClick = onReadClick) {
                Icon(
                    painter = painterResource(R.drawable.outline_ear_sound_24),
                    contentDescription = "Lire le texte"
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = onSpeechToTextClick) {
                Icon(
                    painter = painterResource(R.drawable.outline_mic_24),
                    contentDescription = "Saisie vocale"
                )
            }
        }
    )

}

@Composable
fun AppInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    ttsManager: TextToSpeechManager,
    sttManager: SpeechToTextManager,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        if (!matches.isNullOrEmpty()) {
            onValueChange(matches[0])
        }
    }

    Input(
        value = value,
        onValueChange = onValueChange,
        label = label,
        keyboardType = keyboardType,
        onReadClick = { ttsManager.speak(value) },
        onSpeechToTextClick = { sttManager.startSpeechToText(speechLauncher) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelection(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>
){
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(50)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }

}

@Composable
fun DateInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        LaunchedEffect(Unit) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val dateStr = "%02d/%02d/%d".format(selectedDay, selectedMonth + 1, selectedYear)
                    onValueChange(dateStr)
                    showDialog = false
                },
                year,
                month,
                day
            ).apply {
                setOnDismissListener { showDialog = false }
            }.show()
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        shape = RoundedCornerShape(50)
    )
}






@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AppSelectionPreview(){
    ProjetDeVieTheme {
        var genre by remember { mutableStateOf("") }
        val genres = listOf("Homme", "Femme")
        AppSelection (
            value = genre,
            onValueChange = { genre = it },
            label = "Genre",
            options = genres
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DateInputPreview(){
    ProjetDeVieTheme {
        var dateNaissance by remember { mutableStateOf("") }
        DateInput(
            value = dateNaissance,
            onValueChange = { dateNaissance = it },
            label = "Date de naissance (JJ/MM/AAAA)"
        )
    }
}