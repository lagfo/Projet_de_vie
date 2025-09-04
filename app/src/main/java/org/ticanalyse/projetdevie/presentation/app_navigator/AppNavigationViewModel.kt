package org.ticanalyse.projetdevie.presentation.app_navigator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.domain.usecase.user.GetCurrentUserUseCase
import org.ticanalyse.projetdevie.utils.Result
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
): ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _resumeUri = MutableStateFlow<String>("")
    val resumeUri: StateFlow<String> = _resumeUri

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getCurrentUser()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = getCurrentUserUseCase()) {
                is Result.Error -> {
                    _currentUser.value = null
                    _isLoading.value = false
                    Timber.d("Erreur lors de la récupération de l'utilisateur: ${result.message}")
                }
                is Result.Success -> {
                    _currentUser.value = result.data
                    _isLoading.value = false
                }
                is Result.Loading -> { /* Optionnel */ }
            }
        }
    }


    fun refreshCurrentUser() {
        getCurrentUser()
    }

}