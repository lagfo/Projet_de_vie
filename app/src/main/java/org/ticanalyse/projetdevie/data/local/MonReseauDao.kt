package org.ticanalyse.projetdevie.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.ticanalyse.projetdevie.domain.model.MonReseau

@Dao
interface   MonReseauDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(monReseau: MonReseau)


    @Query("SELECT * FROM monreseau")
    suspend fun getMonReseau(): MonReseau

}