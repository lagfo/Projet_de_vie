package org.ticanalyse.projetdevie.presentation.register

import org.ticanalyse.projetdevie.domain.model.User

sealed class RegisterEvent {
    object SaveAppEntry: RegisterEvent()
    data class UpsertUser(val user: User) : RegisterEvent()
}