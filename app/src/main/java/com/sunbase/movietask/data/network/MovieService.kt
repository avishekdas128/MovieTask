package com.sunbase.movietask.data.network

import com.sunbase.movietask.BuildConfig
import com.sunbase.movietask.data.remote.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    companion object {
        const val ENDPOINT = "http://www.omdbapi.com/"
    }

    @GET("?type=movie")
    suspend fun searchMovie(
        @Query("s") searchQuery: String?,
        @Query("page") page: Int? = null,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
    ): MovieResponse
}