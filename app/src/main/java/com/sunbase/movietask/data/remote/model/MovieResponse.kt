package com.sunbase.movietask.data.remote.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("Search") var search: ArrayList<Movie> = arrayListOf(),
    @SerializedName("totalResults") var totalResults: String? = null,
    @SerializedName("Response") var response: String? = null,
    @SerializedName("Error") var error: String? = null
)