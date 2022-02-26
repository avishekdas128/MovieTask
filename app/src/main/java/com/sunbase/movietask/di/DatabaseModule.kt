package com.sunbase.movietask.di

import android.app.Application
import androidx.room.Room
import com.sunbase.movietask.data.db.RecentSearchDAO
import com.sunbase.movietask.data.db.RecentSearchDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application,
        callback: RecentSearchDatabase.Callback
    ): RecentSearchDatabase {
        return Room.databaseBuilder(application, RecentSearchDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideArticleDao(db: RecentSearchDatabase): RecentSearchDAO {
        return db.getArticleDao()
    }
}