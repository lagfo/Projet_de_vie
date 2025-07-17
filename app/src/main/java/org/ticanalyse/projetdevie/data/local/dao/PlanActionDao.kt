package org.ticanalyse.projetdevie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.ticanalyse.projetdevie.domain.model.PlanAction
import org.ticanalyse.projetdevie.domain.model.ProjectInfo
import org.ticanalyse.projetdevie.domain.model.Skill


@Dao
interface PlanActionDao {
    @Query("SELECT * FROM PlanAction")
    fun getPlanAction(): Flow<List<PlanAction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanAction(planAction: PlanAction)
}