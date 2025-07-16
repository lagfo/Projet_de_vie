package org.ticanalyse.projetdevie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.ticanalyse.projetdevie.domain.model.ProjectInfo
import org.ticanalyse.projetdevie.domain.model.Skill


@Dao
interface PlanificationProjetDao {
    @Query("SELECT * FROM ProjectInfo")
    fun getProjectInfo(): Flow<List<ProjectInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjectInfo(projectInfo: ProjectInfo)
}