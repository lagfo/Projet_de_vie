package org.ticanalyse.projetdevie.domain.usecase.ligne_de_vie_usecase

import jakarta.inject.Inject
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository.LigneDeVieElementRepository

class AddLigneDeVieElement @Inject constructor(
    private val repository: LigneDeVieElementRepository
) {
    suspend operator fun invoke(ligneDeVieElement: Element)=repository.insertLigneDeVieElement(ligneDeVieElement)
}