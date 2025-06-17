package org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository

import kotlinx.coroutines.flow.Flow
import org.ticanalyse.projetdevie.domain.model.Element

interface LigneDeVieElementRepository {
    suspend fun insertLigneDeVieElement(element: Element)

      fun getElements(): Flow<List<Element>>
}