package org.ticanalyse.projetdevie.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.ticanalyse.projetdevie.domain.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract val appDao: AppDao

}