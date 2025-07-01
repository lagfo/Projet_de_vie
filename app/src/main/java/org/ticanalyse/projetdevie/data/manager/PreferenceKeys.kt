package org.ticanalyse.projetdevie.data.manager

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import org.ticanalyse.projetdevie.utils.Constants

object PreferenceKeys {

    val APP_ENTRY = booleanPreferencesKey(Constants.APP_ENTRY)
    val nom = stringPreferencesKey("user_nom")
    val prenom = stringPreferencesKey("user_prenom")
    val dateNaissance = stringPreferencesKey("user_dateNaissance")
    val numTel = stringPreferencesKey("user_numTel")
    val genre = stringPreferencesKey("user_genre")
    val email = stringPreferencesKey("user_email")
    val avatarUri = stringPreferencesKey("user_avatarUri")
}