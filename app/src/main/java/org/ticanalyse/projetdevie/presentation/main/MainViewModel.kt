package org.ticanalyse.projetdevie.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.ticanalyse.projetdevie.domain.usecase.app_entry.AppEntryUseCases
import org.ticanalyse.projetdevie.presentation.nvgraph.Route
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    appEntryUseCases: AppEntryUseCases
): ViewModel() {
    var splashCondition by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf(Route.AppStartNavigation.route)
        private set

    init {
        appEntryUseCases.readAppEntry().onEach {
                shouldStartFromHomeScreen ->
            Timber.tag("tag").d("$shouldStartFromHomeScreen")

            startDestination = if(shouldStartFromHomeScreen){
                Route.AppNavigation.route
            }else{
                Route.AppStartNavigation.route
            }
            delay(300)
            splashCondition = false
        }.launchIn(viewModelScope)

    }

}