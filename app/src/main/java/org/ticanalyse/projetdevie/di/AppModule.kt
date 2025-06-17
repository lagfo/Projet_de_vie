package org.ticanalyse.projetdevie.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.ticanalyse.projetdevie.data.local.AppDao
import org.ticanalyse.projetdevie.data.local.AppDatabase
import org.ticanalyse.projetdevie.data.local.MonReseauDao
import org.ticanalyse.projetdevie.data.manager.LocalUserManagerImpl
import org.ticanalyse.projetdevie.data.repository.UserRepositoryImpl
import org.ticanalyse.projetdevie.data.manager.LocalUserManager
import org.ticanalyse.projetdevie.data.manager.dataStore
import org.ticanalyse.projetdevie.data.repository.UserRepository
import org.ticanalyse.projetdevie.domain.repository.MonReseauRepository
import org.ticanalyse.projetdevie.domain.usecase.app_entry.AppEntryUseCases
import org.ticanalyse.projetdevie.domain.usecase.app_entry.ReadAppEntry
import org.ticanalyse.projetdevie.domain.usecase.app_entry.SaveAppEntry
import org.ticanalyse.projetdevie.domain.usecase.mon_reseau.GetMonReseau
import org.ticanalyse.projetdevie.domain.usecase.mon_reseau.MonReseauUseCases
import org.ticanalyse.projetdevie.domain.usecase.mon_reseau.UpsertMonReseau
import org.ticanalyse.projetdevie.domain.usecase.user.GetUser
import org.ticanalyse.projetdevie.domain.usecase.user.UpsertUser
import org.ticanalyse.projetdevie.domain.usecase.user.UserUseCases
import org.ticanalyse.projetdevie.utils.Constants.APP_DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application
    ): LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun ProvidesDataStore(
        @ApplicationContext app: Context
    ) = app.dataStore

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManager: LocalUserManager
    ) = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )

    @Provides
    @Singleton
    fun provideUserRepository(
        appDao: AppDao,
        dataStore: DataStore<Preferences>
    ) : UserRepository = UserRepositoryImpl(appDao = appDao, dataStore = dataStore)

    @Provides
    @Singleton
    fun provideUserUseCases(
        userRepository: UserRepository
    ) : UserUseCases{
        return UserUseCases(
            getUser = GetUser(userRepository),
            upsertUser = UpsertUser(userRepository)
        )
    }

    @Provides
    @Singleton
    fun provideMonReseauUseCases(
        monReseauRepository: MonReseauRepository
    ) : MonReseauUseCases{
        return MonReseauUseCases(
            getMonReseau = GetMonReseau(monReseauRepository),
            upsertMonReseau = UpsertMonReseau(monReseauRepository)
        )
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application
    ): AppDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = AppDatabase::class.java,
            name = APP_DATABASE_NAME
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    @Singleton
    fun provideAppDao(
        appDatabase: AppDatabase
    ): AppDao = appDatabase.appDao

    @Provides
    @Singleton
    fun provideMonReseauDao(
        appDatabase: AppDatabase
    ): MonReseauDao = appDatabase.monReseauDao

}