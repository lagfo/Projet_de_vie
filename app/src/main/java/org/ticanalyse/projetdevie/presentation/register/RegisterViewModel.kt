package org.ticanalyse.projetdevie.presentation.register

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
import org.ticanalyse.projetdevie.domain.usecase.app_entry.AppEntryUseCases
import org.ticanalyse.projetdevie.domain.usecase.user.GetCurrentUserUseCase
import org.ticanalyse.projetdevie.domain.usecase.user.GetResumeUriUseCase
import org.ticanalyse.projetdevie.domain.usecase.user.SetCurrentUserUseCase
import org.ticanalyse.projetdevie.domain.usecase.user.SetResumeUriUseCase
import org.ticanalyse.projetdevie.domain.usecase.user.UserUseCases
import org.ticanalyse.projetdevie.utils.Result
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases,
    private val userUseCases: UserUseCases,
    private val setCurrentUserUseCase: SetCurrentUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
): ViewModel()  {

    fun onEvent(event: RegisterEvent){
        when(event){
            is RegisterEvent.SaveAppEntry ->{
                saveUserEntry()
            }

            is RegisterEvent.UpsertUser->{
                upsertUser(event.user)
            }
        }
    }

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


    fun logout() {
        viewModelScope.launch {
            when (val result = setCurrentUserUseCase(null)) {
                is Result.Error -> {
                    Timber.d("Erreur lors de la modification de l'utilisateur: ${result.message}")
                }
                is Result.Success -> {
                    _currentUser.value = result.data
                }
                is Result.Loading -> {
                }
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

    private fun saveUserEntry() {
        viewModelScope.launch {
            appEntryUseCases.saveAppEntry()
        }
    }

    private  fun upsertUser(user: User) {
        viewModelScope.launch {
            userUseCases.upsertUser(user = user)
        }
    }


}

