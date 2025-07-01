package org.ticanalyse.projetdevie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.ticanalyse.projetdevie.domain.model.Skill

@Dao
interface SkillsDao {

    @Query("SELECT * FROM skill")
    suspend fun getSelectedSkills(): Skill?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(skill: Skill)

}