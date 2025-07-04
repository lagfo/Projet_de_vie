package org.ticanalyse.projetdevie.domain.repository.lienVieReelRepository

import org.ticanalyse.projetdevie.data.local.dao.LienVieReelDao
import org.ticanalyse.projetdevie.domain.model.LienVieReel
import javax.inject.Inject

class LienVieReelRepository @Inject constructor(
    private val lienVieReelDao: LienVieReelDao
) {
    suspend fun insertLienVieReel(lienVieReel: LienVieReel)=lienVieReelDao.insertLienVieReel(lienVieReel)
    fun getLineLienVieReel()=lienVieReelDao.getLine()
}