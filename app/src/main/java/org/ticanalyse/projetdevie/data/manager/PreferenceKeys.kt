package org.ticanalyse.projetdevie.data.manager

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import org.ticanalyse.projetdevie.utils.Constants

object PreferenceKeys {

    val APP_ENTRY = booleanPreferencesKey(Constants.APP_ENTRY)
    val nom = stringPreferencesKey("user_nom")
    val prenom = stringPreferencesKey("user_prenom")
    val age = stringPreferencesKey("user_age")
    val numTel = stringPreferencesKey("user_numTel")
    val genre = stringPreferencesKey("user_genre")
    val avatarUri = stringPreferencesKey("user_avatarUri")
}