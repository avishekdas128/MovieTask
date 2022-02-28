package com.sunbase.movietask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sunbase.movietask.data.local.model.RecentSearch
import com.sunbase.movietask.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [RecentSearch::class], version = 2, exportSchema = false)
abstract class RecentSearchDatabase : RoomDatabase() {

    abstract fun getRecentSearchDAO(): RecentSearchDAO

    class Callback @Inject constructor(
        private val database: Provider<RecentSearchDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}