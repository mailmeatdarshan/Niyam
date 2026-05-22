package com.example.niyam.di

import android.content.Context
import androidx.room.Room
import com.example.niyam.data.local.NiyamDatabase
import com.example.niyam.data.local.RoutineDao
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
    fun provideDatabase(@ApplicationContext context: Context): NiyamDatabase {
        return Room.databaseBuilder(
            context,
            NiyamDatabase::class.java,
            "niyam_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideRoutineDao(database: NiyamDatabase): RoutineDao {
        return database.routineDao()
    }
}
