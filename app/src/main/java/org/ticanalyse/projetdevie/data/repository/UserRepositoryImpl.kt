package org.ticanalyse.projetdevie.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.ticanalyse.projetdevie.data.local.dao.AppDao
import org.ticanalyse.projetdevie.data.manager.PreferenceKeys
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.utils.Result
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val appDao: AppDao,
    private val dataStore: DataStore<Preferences>
) : UserRepository {

    override suspend fun setCurrentUser(user: User?): Result<User?> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferenceKeys.nom] = user?.nom ?: ""
                preferences[PreferenceKeys.prenom] = user?.prenom ?: ""
                preferences[PreferenceKeys.dateNaissance] = user?.dateNaissance ?: ""
                preferences[PreferenceKeys.numTel] = user?.numTel ?: ""
                preferences[PreferenceKeys.genre] = user?.genre ?: ""
                preferences[PreferenceKeys.avatarUri] = user?.avatarUri ?: ""
                preferences[PreferenceKeys.email] = user?.email ?: ""
            }
            if (user != null) {
                appDao.upsert(user)
            }
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error("Erreur Lors de la modification de l'utilisateur: ${e.message}")
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return try {
            val currentUser = dataStore.data.map { preferences ->
                User(
                    nom = preferences[PreferenceKeys.nom] ?: "",
                    prenom = preferences[PreferenceKeys.prenom] ?: "",
                    dateNaissance = preferences[PreferenceKeys.dateNaissance] ?: "",
                    numTel = preferences[PreferenceKeys.numTel] ?: "",
                    genre = preferences[PreferenceKeys.genre] ?: "",
                    avatarUri = preferences[PreferenceKeys.avatarUri] ?: "",
                    email = preferences[PreferenceKeys.email] ?: "",
                )
            }.first()
            Result.Success(if (currentUser.nom.isNotEmpty() && currentUser.prenom.isNotEmpty()) currentUser else null)
        } catch (e: Exception) {
            Result.Error("Erreur Lors de la récupération de l'utilisateur: ${e.message}")
        }
    }


    override suspend fun upsertUser(user: User) {
        appDao.upsert(user)
    }

    override suspend fun setResumeUri(uri: String, module: String) {
        dataStore.edit { preferences ->
            when (module) {
                "lienVieReel" -> preferences[PreferenceKeys.lienVieReelUri] = uri
                else -> preferences[PreferenceKeys.resumeUri] = uri
            }
        }
    }

    override suspend fun getResumeUri(module: String): String {
        return try {
            dataStore.data.map { preferences ->
                when (module) {
                    "lienVieReel" -> preferences[PreferenceKeys.lienVieReelUri] ?: ""
                    "planificationProjet" -> preferences[PreferenceKeys.planifierProjet] ?: ""
                    else -> preferences[PreferenceKeys.resumeUri] ?: ""
                }
            }.first()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    override suspend fun getUser(): User {
        return appDao.getUser()
    }

}