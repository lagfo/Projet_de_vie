package org.ticanalyse.projetdevie.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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
): ViewModel()  {


    private var _currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val currentUser = _currentUser.asStateFlow()
    private var _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading = _isLoading.onStart { getCurrentUser() }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), true)

    fun onSubmit(user: User?) {
        viewModelScope.launch {
            when (val result = setCurrentUserUseCase(user)) {
                is Result.Error -> {
                    Timber.d("Erreur lors de la modification de l'utilisateur: ${result.message}")
                }
                is Result.Success -> {
                    _currentUser.value = result.data
                }
                is Result.Loading -> {}
            }
        }
    }

    fun getCurrentUser() {
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



}

