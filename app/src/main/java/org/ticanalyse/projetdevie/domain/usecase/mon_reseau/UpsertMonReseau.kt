package org.ticanalyse.projetdevie.domain.usecase.mon_reseau

import org.ticanalyse.projetdevie.domain.model.MonReseau
import org.ticanalyse.projetdevie.domain.repository.MonReseauRepository

class UpsertMonReseau(
    private val monReseauRepository: MonReseauRepository
) {
    suspend operator fun invoke(monReseau: MonReseau){
        monReseauRepository.upsertMonReseau(monReseau)
    }
}