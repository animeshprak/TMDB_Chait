package com.chaitanya.tmdb.listeners

interface LazyMoviesLoading {
    fun onMoviesLoadingRequired(currentVisibleItem: Int)
}