package org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository

import jakarta.inject.Inject
import org.ticanalyse.projetdevie.data.local.dao.LigneDeVieDao
import org.ticanalyse.projetdevie.domain.model.Element

class LigneDeVieRepository @Inject constructor(
    private val ligneDeVieDao: LigneDeVieDao
)
{
    suspend fun insertLigneDeVieElement(element: Element)=ligneDeVieDao.insertElement(element)
    fun getElements()=ligneDeVieDao.getElement()
    fun getPassedElements()=ligneDeVieDao.getPassedElement()
    fun getPresentElements()=ligneDeVieDao.getPresentElement()
    fun getElementById(id:Int)=ligneDeVieDao.getElementById(id)

}