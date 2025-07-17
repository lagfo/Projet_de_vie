package org.ticanalyse.projetdevie.domain.usecase.user

import org.ticanalyse.projetdevie.data.repository.UserRepository
import org.ticanalyse.projetdevie.data.repository.UserRepositoryImpl
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.utils.Result
import javax.inject.Inject

class GetResumeUriUseCase @Inject constructor(
    private val repository: UserRepository
){
    suspend operator fun invoke(module: String): String {
        return repository.getResumeUri(module)
    }
}