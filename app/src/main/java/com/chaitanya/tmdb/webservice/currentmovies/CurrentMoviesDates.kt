package com.chaitanya.tmdb.webservice.currentmovies

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrentMoviesDates {

    @SerializedName("maximum")
    @Expose
    var maximum: String? = null
    @SerializedName("minimum")
    @Expose
    var minimum: String? = null

}