package org.ticanalyse.projetdevie.utils

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResultLauncher
import java.util.Locale

class SpeechToTextManager {

    fun startSpeechToText(
        launcher: ActivityResultLauncher<Intent>,
        prompt: String = "Parlez maintenant"
    ) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, prompt)
        }
        launcher.launch(intent)
    }
}