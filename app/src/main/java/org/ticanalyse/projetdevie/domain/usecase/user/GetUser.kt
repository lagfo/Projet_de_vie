package org.ticanalyse.projetdevie.domain.usecase.user

import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.domain.repository.UserRepository

class GetUser (
    private val userRepository: UserRepository
){

    suspend operator fun invoke():User{
        return userRepository.getUser()
    }
}