package org.ticanalyse.projetdevie.presentation.register

sealed class RegisterEvent {
    object SaveAppEntry: RegisterEvent()
}