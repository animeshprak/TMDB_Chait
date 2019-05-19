package com.chaitanya.tmdb.utils

class AppConstants {
    companion object {
        const val API_SECRET = "ZWNiNjc0ZTAxM2FkOGFjMWYyZmVhYTlkZDhhOTgxMmI="
        const val API_ACCESS_TOKEN_AUTH = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlY2I2NzRlMDEzYWQ4YWMxZjJmZWFhOWRkOGE5ODEyYiIsInN1YiI6IjVjZGI4OTcyMGUwYTI2Mzg2OGQyNGRiNCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.0oVxMQGMbRVVhXrk9rjJfsipiBn6fx7hR4KIo8hK57s"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/%s/%s"
        const val IMAGE_SIZE_ORIGINAL = "original"
        const val IMAGE_SIZE_W500 = "w500"
        const val IMAGE_SIZE_W400 = "w400"
        const val IMAGE_SIZE_W300 = "w300"
        const val MOVIE_ID = "movie_id"
        const val MOVIE_NAME = "movie_name"
        const val MOVIE_OVERVIEW = "movie_overview"
        const val MOVIE_RELEASE_DATE = "movie_release_date"
        const val BUNDLE = "bundle"

        const val BASE_URL = "https://api.themoviedb.org/"
        const val SEARCH_URL = "3/search/movie"
        const val TRENDING_URL = "w300"
        const val MOVIES_DETAIL_URL = "%s"
        const val NOW_PLAYING_URL = "3/movie/now_playing"
        const val POPULAR_MOVIES_URL = "3/movie/popular"
        const val TOP_RATED_MOVIES_URL = "3/movie/top_rated"
        const val MOVIES_VIDEO_URL = "3/movie/%s/videos"
        //const val MOVIES_LANGUAGE = "en-US"
        const val MOVIES_LANGUAGE = ""
    }

}