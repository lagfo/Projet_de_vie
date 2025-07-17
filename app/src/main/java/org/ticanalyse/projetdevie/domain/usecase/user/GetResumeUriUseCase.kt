package org.ticanalyse.projetdevie.domain.usecase.user

import org.ticanalyse.projetdevie.data.repository.UserRepositoryImpl
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.utils.Result
import javax.inject.Inject

class GetResumeUriUseCase @Inject constructor(
    private val repository: UserRepositoryImpl
){
    suspend operator fun invoke(): String {
        return repository.getResumeUri()
    }
}