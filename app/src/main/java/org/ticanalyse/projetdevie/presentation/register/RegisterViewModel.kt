package org.ticanalyse.projetdevie.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.domain.usecase.app_entry.AppEntryUseCases
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
): ViewModel()  {

    fun onEvent(event: RegisterEvent){
        when(event){
            is RegisterEvent.SaveAppEntry ->{
                saveUserEntry()
            }
        }
    }

    private fun saveUserEntry() {
        viewModelScope.launch {
            appEntryUseCases.saveAppEntry()
        }
    }

}