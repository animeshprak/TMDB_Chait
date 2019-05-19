package com.chaitanya.tmdb.listeners

import com.chaitanya.tmdb.webservice.currentmovies.CurrentMoviesData

interface UserTapOnMovie {
    fun onUserTapOnMovies(currentMoviesData: CurrentMoviesData)
}