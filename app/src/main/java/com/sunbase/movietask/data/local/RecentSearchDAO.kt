package com.sunbase.movietask.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sunbase.movietask.data.local.model.RecentSearch

@Dao
interface RecentSearchDAO {

    @Transaction
    suspend fun updateRecentSearch(recent: RecentSearch) {
        recent.let {
            val item =
                checkItemExists(recent.search) // This ensure that no recent search is repeated in the list.
            if (item == null) {
                insertRecentSearch(recent)
                deleteOldSearch() // This deletes previous searches to keep the database limited to 10.
            }
        }
    }

    @Query("SELECT * from recent_search WHERE search= :search LIMIT 1")
    fun checkItemExists(search: String): RecentSearch?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSearch(search: RecentSearch)

    @Query("DELETE FROM recent_search where id NOT IN (SELECT id from recent_search ORDER BY id DESC LIMIT 10)")
    suspend fun deleteOldSearch()

    @Query("SELECT * FROM recent_search ORDER BY id DESC")
    suspend fun getRecentSearches(): List<RecentSearch>

    @Query("SELECT * FROM recent_search ORDER BY id DESC")
    fun getTestRecentSearches(): LiveData<List<RecentSearch>>
}