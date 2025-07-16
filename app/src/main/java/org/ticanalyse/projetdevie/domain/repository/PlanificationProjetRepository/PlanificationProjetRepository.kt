package org.ticanalyse.projetdevie.domain.repository.PlanificationProjetRepository

import jakarta.inject.Inject
import org.ticanalyse.projetdevie.data.local.dao.LigneDeVieDao
import org.ticanalyse.projetdevie.data.local.dao.PlanificationProjetDao
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.model.ProjectInfo

class PlanificationProjetRepository @Inject constructor(
    private val planificationProjetDao: PlanificationProjetDao
)
{
    suspend fun insertProjectInfo(projectInfo: ProjectInfo)=planificationProjetDao.insertProjectInfo(projectInfo)
    fun getProjectInfo()=planificationProjetDao.getProjectInfo()


}