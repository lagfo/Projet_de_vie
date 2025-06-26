package org.ticanalyse.projetdevie.data.repository

import org.ticanalyse.projetdevie.data.local.dao.MonReseauDao
import org.ticanalyse.projetdevie.domain.model.MonReseau
import org.ticanalyse.projetdevie.domain.repository.MonReseauRepository

class MonReseauRepositoryImpl(
    private val monReseauDao: MonReseauDao
) : MonReseauRepository {
    override suspend fun upsertMonReseau(monReseau: MonReseau) {
        monReseauDao.upsert(monReseau)
    }
    override suspend fun getMonReseau(): MonReseau? {
        return monReseauDao.getMonReseau()
    }
}