package org.ticanalyse.projetdevie.domain.repository

import org.ticanalyse.projetdevie.domain.model.User

interface UserRepository {
    suspend fun upsertArticle(user: User)


    suspend fun getUser(): User
}