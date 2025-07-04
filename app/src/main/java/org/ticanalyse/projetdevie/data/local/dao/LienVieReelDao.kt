package org.ticanalyse.projetdevie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.model.LienVieReel

@Dao
interface LienVieReelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLienVieReel(lienVieReel: LienVieReel)

    @Query("SELECT * FROM  LienVieReel")
    fun getLine(): Flow<List<LienVieReel>>

}