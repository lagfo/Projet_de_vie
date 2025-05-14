package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import org.ticanalyse.projetdevie.utils.TextToSpeechManager

@Composable
fun appTTSManager() : TextToSpeechManager {
    val context = LocalContext.current
    val ttsManager = remember {
        TextToSpeechManager(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            ttsManager.shutdown()
        }
    }

    return ttsManager
}