package org.ticanalyse.projetdevie.data.repository

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import org.ticanalyse.projetdevie.data.local.ReponseQuestionLigneDeVieDao
import org.ticanalyse.projetdevie.domain.model.ReponseQuestionLigneDeVie
import org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository.ReponseQuestionLigneDeVieRepository

class ReponseLigneDeVieRepositoryImpl @Inject constructor(
    private val reponseQuestionLigneDeVieDao: ReponseQuestionLigneDeVieDao
): ReponseQuestionLigneDeVieRepository {
    override suspend fun insertResponse(reponseQuestionLigneDeVie: ReponseQuestionLigneDeVie) {
        reponseQuestionLigneDeVieDao.insertResponse(reponseQuestionLigneDeVie)
    }

    override fun getResponse(): Flow<List<ReponseQuestionLigneDeVie>> {
        return reponseQuestionLigneDeVieDao.getResponse()
    }
}