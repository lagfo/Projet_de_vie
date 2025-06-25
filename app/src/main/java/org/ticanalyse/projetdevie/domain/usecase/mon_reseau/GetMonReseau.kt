package org.ticanalyse.projetdevie.domain.usecase.mon_reseau

import org.ticanalyse.projetdevie.domain.model.MonReseau
import org.ticanalyse.projetdevie.domain.repository.MonReseauRepository

class GetMonReseau(
    private val monReseauRepository: MonReseauRepository
) {
    suspend operator fun invoke(): MonReseau? {
        return monReseauRepository.getMonReseau()
    }
}