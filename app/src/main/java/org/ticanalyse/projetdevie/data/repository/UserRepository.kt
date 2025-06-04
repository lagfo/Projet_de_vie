package org.ticanalyse.projetdevie.data.repository

import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.utils.Result

interface UserRepository {

    suspend fun setCurrentUser(user: User?): org.ticanalyse.projetdevie.utils.Result<User?>

    suspend fun getCurrentUser(): Result<User?>

    suspend fun upsertUser(user: User)

    suspend fun getUser(): User

}