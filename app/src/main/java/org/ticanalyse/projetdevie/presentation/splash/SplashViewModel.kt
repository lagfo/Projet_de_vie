package org.ticanalyse.projetdevie.presentation.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.ticanalyse.projetdevie.domain.usecase.app_entry.AppEntryUseCases
import org.ticanalyse.projetdevie.presentation.nvgraph.Route
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
): ViewModel() {

    val userEntry = appEntryUseCases.readAppEntry().stateIn(viewModelScope, SharingStarted.Lazily, null)

    /*var loginCondition by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf(Route.LoginScreen.route)
        private set

    init {
        appEntryUseCases.readAppEntry().onEach {
                shouldStartFromLoginScreen ->
            startDestination = if(shouldStartFromLoginScreen){
                Route.LoginScreen.route

            }else{
                loginCondition= false
                Route.RegisterScreen.route
            }
            delay(300)
        }.launchIn(viewModelScope)

    }

     */

}