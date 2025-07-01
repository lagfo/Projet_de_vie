package org.ticanalyse.projetdevie.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.domain.usecase.app_entry.AppEntryUseCases
import org.ticanalyse.projetdevie.domain.usecase.user.GetCurrentUserUseCase
import org.ticanalyse.projetdevie.domain.usecase.user.SetCurrentUserUseCase
import org.ticanalyse.projetdevie.domain.usecase.user.UserUseCases
import org.ticanalyse.projetdevie.utils.Result
//import org.ticanalyse.projetdevie.presentation.nvgraph.Route
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    appEntryUseCases: AppEntryUseCases,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {
    var splashCondition by mutableStateOf(true)
        private set

    val visiblePermissionDialogQueue = mutableListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeLast()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

//    var startDestination by mutableStateOf(Route.AppStartNavigation.route)
//        private set

    init {
        appEntryUseCases.readAppEntry().onEach {
                shouldStartFromHomeScreen ->
            Timber.tag("tag").d("$shouldStartFromHomeScreen")

//            startDestination = if(shouldStartFromHomeScreen){
//                Route.AppNavigation.route
//            }else{
//                Route.AppStartNavigation.route
//            }
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




}