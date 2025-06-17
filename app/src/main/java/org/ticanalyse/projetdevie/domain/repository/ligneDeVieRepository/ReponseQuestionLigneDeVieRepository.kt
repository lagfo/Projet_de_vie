package org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository

import kotlinx.coroutines.flow.Flow
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.model.ReponseQuestionLigneDeVie

interface ReponseQuestionLigneDeVieRepository {
    suspend fun insertResponse(reponseQuestionLigneDeVie: ReponseQuestionLigneDeVie)

    fun getResponse():Flow<List<ReponseQuestionLigneDeVie>>
}