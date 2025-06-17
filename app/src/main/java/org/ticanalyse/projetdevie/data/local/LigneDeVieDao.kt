package org.ticanalyse.projetdevie.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.ticanalyse.projetdevie.domain.model.Element

@Dao
interface LigneDeVieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElement(element: Element)

    @Query("SELECT * FROM  Element")
    fun getElement(): Flow<List<Element>>
}