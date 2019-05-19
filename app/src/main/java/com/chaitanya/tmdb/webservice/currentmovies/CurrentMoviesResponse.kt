package com.chaitanya.tmdb.webservice.currentmovies

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrentMoviesResponse {

    @SerializedName("results")
    @Expose
    var results: List<CurrentMoviesData>? = null
    @SerializedName("page")
    @Expose
    var page: Int? = null
    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null
    @SerializedName("dates")
    @Expose
    var dates: CurrentMoviesDates? = null
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null

}