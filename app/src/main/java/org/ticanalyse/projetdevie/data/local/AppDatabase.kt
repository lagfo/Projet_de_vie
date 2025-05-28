package org.ticanalyse.projetdevie.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ticanalyse.projetdevie.domain.model.MonReseau
import org.ticanalyse.projetdevie.domain.model.User

@Database(entities = [User::class,MonReseau::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract val appDao: AppDao
    abstract val monReseauDao: MonReseauDao

}