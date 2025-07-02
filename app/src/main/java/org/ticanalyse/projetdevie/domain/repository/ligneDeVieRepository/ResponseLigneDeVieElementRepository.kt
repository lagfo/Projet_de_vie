package org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository

import jakarta.inject.Inject
import org.ticanalyse.projetdevie.data.local.dao.ReponseQuestionLigneDeVieDao
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.model.ReponseQuestionLigneDeVie

class ResponseLigneDeVieElementRepository @Inject constructor(
    private val responseLigneDeVieDao: ReponseQuestionLigneDeVieDao
) {
    suspend fun insertResponseLigneDevie(responseQuestionLigneDeVie: ReponseQuestionLigneDeVie)=responseLigneDeVieDao.insertResponse(responseQuestionLigneDeVie)
    fun getResponse()=responseLigneDeVieDao.getResponse()

}