package org.ticanalyse.projetdevie.domain.repository

import org.ticanalyse.projetdevie.domain.model.MonReseau

interface MonReseauRepository {
    suspend fun upsertMonReseau(monReseau: MonReseau)


    suspend fun getMonReseau(): MonReseau
}