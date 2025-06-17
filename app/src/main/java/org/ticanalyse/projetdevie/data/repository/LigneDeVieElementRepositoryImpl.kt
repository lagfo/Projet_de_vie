package org.ticanalyse.projetdevie.data.repository

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import org.ticanalyse.projetdevie.data.local.LigneDeVieDao
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository.LigneDeVieElementRepository

data class LigneDeVieElementRepositoryImpl @Inject constructor(
    private val ligneDeVieDao: LigneDeVieDao
): LigneDeVieElementRepository{

    override suspend fun insertLigneDeVieElement(element: Element) {
        ligneDeVieDao.insertElement(element)
    }

    override fun getElements(): Flow<List<Element>> {
        return ligneDeVieDao.getElement()
    }

}
