package org.ticanalyse.projetdevie.domain.usecase.user

import org.ticanalyse.projetdevie.data.repository.UserRepositoryImpl
import javax.inject.Inject

class SetResumeUriUseCase @Inject constructor(
    private val repository: UserRepositoryImpl
){
    suspend operator fun invoke(uri: String) {
        repository.setResumeUri(uri)
    }
}