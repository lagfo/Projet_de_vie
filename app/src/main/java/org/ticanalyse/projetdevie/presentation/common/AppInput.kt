package org.ticanalyse.projetdevie.presentation.common

import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.date_time.DateTimeDialog
import com.maxkeppeler.sheets.date_time.models.DateTimeConfig
import com.maxkeppeler.sheets.date_time.models.DateTimeSelection
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.utils.Global.validateAge
import org.ticanalyse.projetdevie.utils.Global.validateEmail
import org.ticanalyse.projetdevie.utils.Global.validateNumber
import org.ticanalyse.projetdevie.utils.Global.validateTextEntries
import org.ticanalyse.projetdevie.utils.SpeechToTextManager
import org.ticanalyse.projetdevie.utils.TextToSpeechManager
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun AppInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    inputType: String,
    ttsManager: TextToSpeechManager,
    sttManager: SpeechToTextManager,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    filterInput: ((String) -> String)? = null,
    onSpeechResult: ((String) -> String)? = null,
    onSubmit: Boolean= false,
    isEnable: Boolean = true
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

    val isErrorExist by remember(value, onSubmit, inputType) {
        derivedStateOf {
             onSubmit && when (inputType) {
                "number" -> !validateNumber(value)
                "age" -> !validateAge(value)
                "text" -> !validateTextEntries(value)
                "email" -> !validateEmail(value)
                else -> false
            }
        }
    }


    Column {
        OutlinedTextField(
            enabled = isEnable,
            value = value,
            onValueChange = { newValue ->
                val filtered = filterInput?.invoke(newValue) ?: newValue
                onValueChange(filtered)
            },
            label = { Text(label) },
            supportingText = {
                if (isErrorExist && value.isEmpty()) {
                    Text(text = "Champs requis")
                } else if (isErrorExist && (inputType=="age" || inputType=="number")) {
                    Text(text = "invalide")
                }
            },
            isError = isErrorExist,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            shape = RoundedCornerShape(15),
            leadingIcon = {
                IconButton(onClick = {
                    if (value.isDigitsOnly()) ttsManager.speak(value.chunked(2).joinToString(" "))
                    else ttsManager.speak(value)
                }) {
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
//        if(onSubmit and value.isBlank()){
//            Text(
//                text = "Champs requis",
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.labelSmall,
//                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
//            )
//
//        }else if(value.isNotBlank() && value.isDigitsOnly() && value.length<=1){
//            Text(
//                text = "Valeur invalide",
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.labelSmall,
//                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
//            )
//        }

    }


}


@Composable
fun AppTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    ttsManager: TextToSpeechManager,
    sttManager: SpeechToTextManager,
    onSubmit: Boolean= false,
    isSingleLine: Boolean=false,
    minLine: Int=1
   ) {

    AppInputField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        inputType = "text",
        ttsManager = ttsManager,
        sttManager = sttManager,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        onSubmit=onSubmit,

    )
}

@Composable
fun AppInputFieldMultiLine(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    ttsManager: TextToSpeechManager,
    sttManager: SpeechToTextManager,
    onSubmit: Boolean= false,
    minLines: Int = 3,
    maxLines: Int = 15
) {
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        if (!matches.isNullOrEmpty()) {
            onValueChange("${value.replaceFirstChar { it.uppercaseChar() }} ${matches[0].replaceFirstChar { it.uppercaseChar() }}")
        }
    }

    val isErrorExist by remember (value, onSubmit) {
        derivedStateOf {
            if (onSubmit && value.isBlank()) true
            else false
        }
    }

    Column {
        OutlinedTextField(
            value = value.replaceFirstChar { it.uppercaseChar() },
            onValueChange = { newValue ->
                onValueChange(newValue.replaceFirstChar { it.uppercaseChar() })
            },
            label = { Text(label) },
            supportingText = {
                if (isErrorExist) {
                    Text(text = "Champs requis", color = MaterialTheme.colorScheme.error)
                }
            },
            isError = isErrorExist,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            shape = RoundedCornerShape(15),
            minLines = minLines,
            maxLines = maxLines,
            singleLine = false,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = if (onSubmit) ImeAction.Done else ImeAction.Default,
                keyboardType = KeyboardType.Text
            ),
            visualTransformation = VisualTransformation.None,
            leadingIcon = {
                IconButton(onClick = {
                    if (value.isDigitsOnly()) ttsManager.speak(value.chunked(2).joinToString(" "))
                    else ttsManager.speak(value)
                }) {
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
            },
            colors = OutlinedTextFieldDefaults.colors(

                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer,


                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = MaterialTheme.colorScheme.error,


                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                errorTextColor = MaterialTheme.colorScheme.onErrorContainer,


                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                errorLabelColor = MaterialTheme.colorScheme.error,


                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,


                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,


                cursorColor = MaterialTheme.colorScheme.primary,
                errorCursorColor = MaterialTheme.colorScheme.error
            )

        )
    }
}



@Composable
fun AppBirthDateInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
//    ttsManager: TextToSpeechManager,
//    sttManager: SpeechToTextManager,
    onSubmit: Boolean= false,
    isEnable: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    filterInput: ((String) -> String)? = null,
//    onSpeechResult: ((String) -> String)? = null,

    ) {

//    val speechLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        val data = result.data
//        val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
//        if (!matches.isNullOrEmpty()) {
//            val processed = onSpeechResult?.invoke(matches[0]) ?: matches[0]
//            onValueChange(processed)
//        }
//    }


    val selectedDate = remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    val dateTimeState = rememberUseCaseState()
    DateTimeDialog(
        state = dateTimeState,
        selection = DateTimeSelection.Date { newDate ->
            selectedDate.value = newDate
            onValueChange(selectedDate.value?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString())
        },
        header = Header.Default(
            "Date de naissance",
            IconSource(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Date de naissance"
            )
        ),
        config = DateTimeConfig(
            maxYear = LocalDate.now().year
        )
    )

    val isErrorExist by remember (value, onSubmit) {
        derivedStateOf {
            if (onSubmit && value.isBlank()) true
            else if(validateAge(value)) false
            else false
        }
    }

    OutlinedTextField(
        enabled = isEnable,
        value = value,
        onValueChange = { newValue ->
            val filtered = filterInput?.invoke(newValue) ?: newValue
            onValueChange(filtered)
        },
        label = { Text(label) },
        supportingText = {
            if (isErrorExist && value.isEmpty()) {
                Text(text = "Champs requis")
            } else if (isErrorExist) {
                Text(text = "invalide")
            }
        },
        placeholder = {
            Text("jj/mm/yyyy")
        },
        isError = isErrorExist,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(15),
//        leadingIcon = {
//            IconButton(onClick = {
////                if (value.isDigitsOnly()) ttsManager.speak(value.chunked(2).joinToString(" "))
////                else ttsManager.speak(value)
//                dateTimeState.show()
//            }) {
//                Icon(
//                    imageVector = Icons.Default.DateRange,
//                    contentDescription = "Lire le texte"
//                )
//            }
//        },
        trailingIcon = {
            IconButton(onClick = {
//                if (value.isDigitsOnly()) ttsManager.speak(value.chunked(2).joinToString(" "))
//                else ttsManager.speak(value)
                dateTimeState.show()
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Lire le texte"
                )
            }
        }
    )
}

@Composable
fun   AppAgeInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    ttsManager: TextToSpeechManager,
    sttManager: SpeechToTextManager,
    onSubmit: Boolean= false,
) {

    val isErrorExist by remember { derivedStateOf {
        if (value.isEmpty()) {
            false
        } else {
            validateAge(value)
        }
    } }


    AppInputField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        inputType = "age",
        ttsManager = ttsManager,
        sttManager = sttManager,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        filterInput = { it.filter { c -> c.isDigit() }.take(2) },
        onSpeechResult = { it.filter { c -> c.isDigit() }.take(2) },
        onSubmit = onSubmit
    )
}

@Composable
fun AppPhoneInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    ttsManager: TextToSpeechManager,
    sttManager: SpeechToTextManager,
    onSubmit: Boolean= false
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

    val isErrorExist by remember { derivedStateOf {
        if (value.isEmpty()) {
            false
        } else {
            validateNumber(value)
        }
    } }


    Timber.tag("phone").d(value)

    AppInputField(
        value = value,
        onValueChange = { onValueChange(it.filter { c -> c.isDigit() }) },
        label = label,
        inputType = "number",
        ttsManager = ttsManager,
        sttManager = sttManager,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        visualTransformation = phoneTransformation,
        onSubmit = onSubmit
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelection(
    value: String,
    onValueChange: (String) -> Unit,
    onReadClick: () -> Unit,
    label: String,
    options: List<String>,
    onSubmit: Boolean = false
){
    var expanded by remember { mutableStateOf(false) }
    val isErrorExist by remember (value, onSubmit) {
        derivedStateOf {
            onSubmit && value.isBlank()
        }
    }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        Column {

            OutlinedTextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                },
                isError = isErrorExist,
                supportingText = {
                    if (isErrorExist) {
                        Text(text = "Champs requis")
                    }
                },
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

}