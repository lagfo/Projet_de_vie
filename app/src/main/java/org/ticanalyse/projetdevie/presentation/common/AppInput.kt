package org.ticanalyse.projetdevie.presentation.common

import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.utils.SpeechToTextManager
import org.ticanalyse.projetdevie.utils.TextToSpeechManager


@Composable
fun AppInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    ttsManager: TextToSpeechManager,
    sttManager: SpeechToTextManager,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    filterInput: ((String) -> String)? = null,
    onSpeechResult: ((String) -> String)? = null
) {
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        if (!matches.isNullOrEmpty()) {
            val processed = onSpeechResult?.invoke(matches[0]) ?: matches[0]
            onValueChange(processed)
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            val filtered = filterInput?.invoke(newValue) ?: newValue
            onValueChange(filtered)
        },
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(15),
        leadingIcon = {
            IconButton(onClick = { ttsManager.speak(value) }) {
                Icon(
                    painter = painterResource(R.drawable.outline_ear_sound_24),
                    contentDescription = "Lire le texte"
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = { sttManager.startSpeechToText(speechLauncher) }) {
                Icon(
                    painter = painterResource(R.drawable.outline_mic_24),
                    contentDescription = "Saisie vocale"
                )
            }
        }
    )
}


@Composable
fun AppTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    ttsManager: TextToSpeechManager,
    sttManager: SpeechToTextManager
   ) {
    AppInputField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        ttsManager = ttsManager,
        sttManager = sttManager,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
fun AppAgeInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    ttsManager: TextToSpeechManager,
    sttManager: SpeechToTextManager
) {
    AppInputField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        ttsManager = ttsManager,
        sttManager = sttManager,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        filterInput = { it.filter { c -> c.isDigit() }.take(2) },
        onSpeechResult = { it.filter { c -> c.isDigit() }.take(2) }
    )
}

@Composable
fun AppPhoneInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    ttsManager: TextToSpeechManager,
    sttManager: SpeechToTextManager
) {
    val phoneTransformation = remember {
        object : VisualTransformation {
            override fun filter(text: AnnotatedString): TransformedText {
                val digits = text.text.filter { it.isDigit() }
                val formatted = digits.chunked(2).joinToString(" ")

                val offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int {
                        return (offset + offset / 2).coerceAtMost(formatted.length)
                    }

                    override fun transformedToOriginal(offset: Int): Int {
                        return offset - offset / 3
                    }
                }
                return TransformedText(AnnotatedString(formatted), offsetMapping)
            }
        }
    }

    AppInputField(
        value = value,
        onValueChange = { onValueChange(it.filter { c -> c.isDigit() }) },
        label = label,
        ttsManager = ttsManager,
        sttManager = sttManager,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        visualTransformation = phoneTransformation
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelection(
    value: String,
    onValueChange: (String) -> Unit,
    onReadClick: () -> Unit,
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
            leadingIcon = {
                IconButton(onClick = onReadClick) {
                    Icon(
                        painter = painterResource(R.drawable.outline_ear_sound_24),
                        contentDescription = "Lire le texte"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                .padding(vertical = 4.dp),

            shape = RoundedCornerShape(15)
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