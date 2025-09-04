package org.ticanalyse.projetdevie.presentation.main

//import org.ticanalyse.projetdevie.presentation.nvgraph.Route
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ticanalyse.projetdevie.data.local.AppDatabase
import org.ticanalyse.projetdevie.data.manager.LocalUserManager
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.domain.usecase.app_entry.AppEntryUseCases
import org.ticanalyse.projetdevie.domain.usecase.user.GetCurrentUserUseCase
import org.ticanalyse.projetdevie.utils.Result
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    appEntryUseCases: AppEntryUseCases,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val localUserManager: LocalUserManager,
    private val appDatabase: AppDatabase
): ViewModel() {
    var splashCondition by mutableStateOf(true)
        private set



    init {
        appEntryUseCases.readAppEntry().onEach {
                shouldStartFromHomeScreen ->
            Timber.tag("tag").d("$shouldStartFromHomeScreen")
            delay(300)
            splashCondition = false
        }.launchIn(viewModelScope)

    }

    private var _currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val currentUser = _currentUser.asStateFlow()
    private var _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading = _isLoading.onStart { getUser() }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), true)

    fun getUser(){
        viewModelScope.launch {
            when (val result = getCurrentUserUseCase()) {
                is Result.Error -> {
                    _isLoading.value = false
                    _currentUser.value = null
                    Timber.d("Erreur lors de la récupération de l'utilisateur: ${result.message}")
                }
                is Result.Success -> {
                    _isLoading.value = false
                    _currentUser.value = result.data
                }
                is Result.Loading -> {}
            }
        }
    }

    fun resetDatabase(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {

                    localUserManager.clearPreferences()
                    appDatabase.clearAllTables()

                    // Si vous voulez aussi vider les préférences
                    // appEntryUseCases.clearAppEntry() // à implémenter

                    Timber.d("Toutes les données réinitialisées")

                    // Relancer l'application sur le thread principal
                    withContext(Dispatchers.Main) {
                        restartApp(context)
                    }
                } catch (e: Exception) {
                    Timber.e("Erreur lors de la réinitialisation: ${e.message}")
                }
            }
        }
    }

    private fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
        Runtime.getRuntime().exit(0) // Force la fermeture du processus pour bien relancer à zéro
    }

}