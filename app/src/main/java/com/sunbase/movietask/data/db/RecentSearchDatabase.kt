package com.sunbase.movietask.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sunbase.movietask.data.db.model.RecentSearch
import com.sunbase.movietask.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [RecentSearch::class], version = 2)
abstract class RecentSearchDatabase : RoomDatabase() {

    abstract fun getArticleDao(): RecentSearchDAO

    class Callback @Inject constructor(
        private val database: Provider<RecentSearchDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}