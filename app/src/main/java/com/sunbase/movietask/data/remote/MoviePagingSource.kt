package com.sunbase.movietask.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sunbase.movietask.data.network.MovieService
import com.sunbase.movietask.data.remote.model.Movie
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource constructor(
    private val api: MovieService,
    private val query: String
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = api.searchMovie(query, page)
            if (response.response == "False") LoadResult.Error(Exception(response.error))
            else LoadResult.Page(
                data = response.search,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.search.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(Exception("No network available!"))
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

}