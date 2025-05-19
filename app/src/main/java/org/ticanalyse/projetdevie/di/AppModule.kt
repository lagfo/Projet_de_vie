package org.ticanalyse.projetdevie.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.ticanalyse.projetdevie.data.local.AppDao
import org.ticanalyse.projetdevie.data.local.AppDatabase
import org.ticanalyse.projetdevie.data.manger.LocalUserMangerImpl
import org.ticanalyse.projetdevie.data.repository.UserRepositoryImpl
import org.ticanalyse.projetdevie.domain.manger.LocalUserManger
import org.ticanalyse.projetdevie.domain.repository.UserRepository
import org.ticanalyse.projetdevie.domain.usecase.app_entry.AppEntryUseCases
import org.ticanalyse.projetdevie.domain.usecase.app_entry.ReadAppEntry
import org.ticanalyse.projetdevie.domain.usecase.app_entry.SaveAppEntry
import org.ticanalyse.projetdevie.domain.usecase.user.GetUser
import org.ticanalyse.projetdevie.domain.usecase.user.UpsertUser
import org.ticanalyse.projetdevie.domain.usecase.user.UserUseCases
import org.ticanalyse.projetdevie.utils.Constants.APP_DATABASE_NAME
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application
    ): LocalUserManger = LocalUserMangerImpl(application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManger: LocalUserManger
    ) = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManger),
        saveAppEntry = SaveAppEntry(localUserManger)
    )

    @Provides
    @Singleton
    fun provideUserRepository(
        appDao: AppDao
    ) : UserRepository = UserRepositoryImpl(appDao = appDao)

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
    fun provideAppDatabase(
        application: Application
    ): AppDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = AppDatabase::class.java,
            name = APP_DATABASE_NAME
        ).addTypeConverter(GsonConverterFactory.create())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideAppDao(
        appDatabase: AppDatabase
    ): AppDao = appDatabase.appDao

}