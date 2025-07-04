package org.ticanalyse.projetdevie.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.domain.usecase.user.GetCurrentUserUseCase
import org.ticanalyse.projetdevie.domain.usecase.user.SetCurrentUserUseCase
import org.ticanalyse.projetdevie.utils.Result
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val setCurrentUserUseCase: SetCurrentUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Ajout d'un état pour les messages d'erreur
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Ajout d'un état pour indiquer si la sauvegarde a réussi
    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess

    init {
        getCurrentUser()
    }

    fun onSubmit(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _saveSuccess.value = false

            when (val result = setCurrentUserUseCase(user)) {
                is Result.Error -> {
                    _errorMessage.value = result.message
                    _isLoading.value = false
                    Timber.d("Erreur lors de la modification de l'utilisateur: ${result.message}")
                }
                is Result.Success -> {
                    _currentUser.value = result.data
                    _saveSuccess.value = true
                    _isLoading.value = false
                    Timber.d("Utilisateur modifié avec succès")
                }
                is Result.Loading -> {
                    // Optionnel : gérer l'état de chargement si nécessaire
                }
            }
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = getCurrentUserUseCase()) {
                is Result.Error -> {
                    _currentUser.value = null
                    _errorMessage.value = result.message
                    _isLoading.value = false
                    Timber.d("Erreur lors de la récupération de l'utilisateur: ${result.message}")
                }
                is Result.Success -> {
                    _currentUser.value = result.data
                    _isLoading.value = false
                    Timber.d("Utilisateur récupéré avec succès")
                }
                is Result.Loading -> {
                    // Optionnel
                }
            }
        }
    }

    // Fonction pour effacer les messages d'erreur
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Fonction pour réinitialiser l'état de succès
    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }

    // Fonction pour gérer les événements (si vous voulez utiliser le pattern événements)
    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.UpsertUser -> {
                onSubmit(event.user)
            }
            is ProfileEvent.SaveAppEntry -> {
                // Gérer d'autres événements si nécessaire
            }
        }
    }
}


