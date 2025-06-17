package org.ticanalyse.projetdevie.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.model.ReponseQuestionLigneDeVie

@Dao
interface ReponseQuestionLigneDeVieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(reponseQuestionLigneDeVie: ReponseQuestionLigneDeVie)

    @Query("SELECT * FROM  ReponseQuestionLigneDeVie")
    fun getResponse(): Flow<List<ReponseQuestionLigneDeVie>>
}