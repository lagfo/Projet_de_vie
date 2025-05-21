package org.ticanalyse.projetdevie.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.ticanalyse.projetdevie.domain.usecase.app_entry.AppEntryUseCases
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
): ViewModel() {

    val userEntry = appEntryUseCases.readAppEntry().stateIn(viewModelScope, SharingStarted.Lazily, null)


}