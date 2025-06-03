package org.ticanalyse.projetdevie.domain.usecase.user

import org.ticanalyse.projetdevie.data.repository.UserRepositoryImpl
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.utils.Result
import javax.inject.Inject


class GetCurrentUserUseCase @Inject constructor(
    private val repository: UserRepositoryImpl
) {
    suspend operator fun invoke(): Result<User?> {
        return repository.getCurrentUser()
    }

}