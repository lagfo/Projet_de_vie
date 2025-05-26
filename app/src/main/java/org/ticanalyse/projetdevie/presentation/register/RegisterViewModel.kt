package org.ticanalyse.projetdevie.presentation.register

import android.provider.ContactsContract.CommonDataKinds.Photo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.domain.usecase.app_entry.AppEntryUseCases
import org.ticanalyse.projetdevie.domain.usecase.user.UserUseCases
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases,
    private val userUseCases: UserUseCases
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

