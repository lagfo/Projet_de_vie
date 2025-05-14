package org.ticanalyse.projetdevie.utils

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import timber.log.Timber
import java.util.Locale

class TextToSpeechManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isReady = false
    private val context = context

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val availability = tts?.isLanguageAvailable(Locale.getDefault()) ?: TextToSpeech.LANG_NOT_SUPPORTED
            if (availability == TextToSpeech.LANG_MISSING_DATA || availability == TextToSpeech.LANG_NOT_SUPPORTED) {
                Timber.e("Voix française non disponible ou données manquantes, invitation à l'installation")
                promptInstallTtsData()
                isReady = false
            } else {
                val result = tts?.setLanguage(Locale.getDefault())
                isReady = (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED)
                if (!isReady) {
                    Timber.e("Erreur lors du setLanguage")
                }
            }
        } else {
            Timber.e("Erreur d'initialisation TTS: $status")
            isReady = false
        }
    }

    private fun promptInstallTtsData() {
        try {
            val installIntent = Intent()
            installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
            installIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            this@TextToSpeechManager.context.startActivity(installIntent)
        } catch (e: Exception) {
            Timber.e(e, "Impossible de lancer l'installation des données TTS")
        }
    }

    fun speak(text: String) {
        if (isReady) {
            val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            if (result == TextToSpeech.ERROR) {
                Timber.tag("TextToSpeechManager").e("Erreur lors de la lecture du texte")
            }
        } else {
            Timber.tag("TextToSpeechManager").e("TTS non prêt")
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}