package com.sunbase.movietask.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.sunbase.movietask.data.network.MovieService
import com.sunbase.movietask.data.remote.MoviePagingSource
import com.sunbase.movietask.data.local.model.RecentSearch
import com.sunbase.movietask.data.local.RecentSearchDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val service: MovieService,
    private val dao: RecentSearchDAO
) {

    fun searchMovie(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 3
            )
        ) {
            MoviePagingSource(service, query)
        }.flow

    suspend fun fetchRecentSearch() = dao.getRecentSearches()

    suspend fun addRecentSearch(recentSearch: String) {
        dao.updateRecentSearch(RecentSearch(search = recentSearch))
    }
}