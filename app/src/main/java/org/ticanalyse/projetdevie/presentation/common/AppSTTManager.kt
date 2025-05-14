package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.ticanalyse.projetdevie.utils.SpeechToTextManager

@Composable
fun appSTTManager() : SpeechToTextManager {
    return remember { SpeechToTextManager() }
}