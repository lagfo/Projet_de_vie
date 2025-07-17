package org.ticanalyse.projetdevie.domain.usecase.user

import org.ticanalyse.projetdevie.data.repository.UserRepository
import org.ticanalyse.projetdevie.data.repository.UserRepositoryImpl
import javax.inject.Inject

class SetResumeUriUseCase @Inject constructor(
    private val repository: UserRepository
){
    suspend operator fun invoke(uri: String, module: String) {
        repository.setResumeUri(uri, module)
    }
}