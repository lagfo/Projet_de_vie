package org.ticanalyse.projetdevie.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.ticanalyse.projetdevie.domain.model.User

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: User)


    @Query("SELECT * FROM user")
    suspend fun getUser():User
}