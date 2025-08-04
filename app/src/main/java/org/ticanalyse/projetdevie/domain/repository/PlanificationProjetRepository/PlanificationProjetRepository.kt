package org.ticanalyse.projetdevie.domain.repository.PlanificationProjetRepository

import jakarta.inject.Inject
import org.ticanalyse.projetdevie.data.local.dao.LigneDeVieDao
import org.ticanalyse.projetdevie.data.local.dao.PlanActionDao
import org.ticanalyse.projetdevie.data.local.dao.PlanificationProjetDao
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.model.PlanAction
import org.ticanalyse.projetdevie.domain.model.ProjectInfo

class PlanificationProjetRepository @Inject constructor(
    private val planificationProjetDao: PlanificationProjetDao,
    private val planActionDao: PlanActionDao
)
{
    suspend fun insertProjectInfo(projectInfo: ProjectInfo)=planificationProjetDao.insertProjectInfo(projectInfo)
    fun getProjectInfo()=planificationProjetDao.getProjectInfo()

    suspend fun insertPlanAction(planAction: PlanAction)=planActionDao.insertPlanAction(planAction)
    fun getPlanAction()=planActionDao.getPlanAction()
    fun getPlanActionById(id: Int)=planActionDao.getPlanActionById(id)

    suspend fun deletePlanActionLine(id:Int)=planActionDao.deletePlanActionById(id)


}