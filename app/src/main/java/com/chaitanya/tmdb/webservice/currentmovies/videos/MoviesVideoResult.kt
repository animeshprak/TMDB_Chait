package com.chaitanya.tmdb.webservice.currentmovies.videos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MoviesVideoResult {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("results")
    @Expose
    var results: List<VideoList>? = null

}