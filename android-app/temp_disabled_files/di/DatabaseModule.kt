package com.guardem.app.di

import android.content.Context
import androidx.room.Room
import com.guardem.app.data.local.GuardeMeDatabase
import com.guardem.app.data.local.dao.MemoryDao
import com.guardem.app.data.local.dao.ScheduleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGuardeMeDatabase(
        @ApplicationContext context: Context
    ): GuardeMeDatabase {
        return Room.databaseBuilder(
            context,
            GuardeMeDatabase::class.java,
            "guarde_me_database"
        )
        .fallbackToDestructiveMigration() // Only during development
        .build()
    }

    @Provides
    fun provideMemoryDao(database: GuardeMeDatabase): MemoryDao {
        return database.memoryDao()
    }

    @Provides
    fun provideScheduleDao(database: GuardeMeDatabase): ScheduleDao {
        return database.scheduleDao()
    }
}