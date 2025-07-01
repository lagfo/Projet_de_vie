package org.ticanalyse.projetdevie.presentation.profile

import org.ticanalyse.projetdevie.domain.model.User

sealed class ProfileEvent {
    object SaveAppEntry: ProfileEvent()
    data class UpsertUser(val user: User) : ProfileEvent()
}