package org.ticanalyse.projetdevie.domain.usecase.user

import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.data.repository.UserRepository

class UpsertUser (
    private val userRepository: UserRepository
){
    suspend operator fun invoke(user: User){
        userRepository.upsertUser(user)
    }
}