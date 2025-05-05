package com.swanky.teachit.di

import android.content.Context
import androidx.room.Room
import com.swanky.teachit.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "AppDatabase"
        ).build()
    }

    @Singleton
    @Provides
    fun provideDao(appDatabase: AppDatabase) = appDatabase.getDao()

}