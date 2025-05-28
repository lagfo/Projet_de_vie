package org.ticanalyse.projetdevie.data.repository

import org.ticanalyse.projetdevie.data.local.AppDao
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.domain.repository.UserRepository

class UserRepositoryImpl(
    private val appDao: AppDao
) : UserRepository{

    override suspend fun upsertUser(user: User) {
        appDao.upsert(user)
    }

    override suspend fun getUser(): User {
        return appDao.getUser()
    }

}