package com.bylancer.classified.bylancerclassified.webservices

import com.chaitanya.tmdb.utils.AppConstants
import com.chaitanya.tmdb.webservice.currentmovies.CurrentMoviesResponse
import com.chaitanya.tmdb.webservice.currentmovies.videos.MoviesVideoResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface WebServiceApiInterface {

    @GET(AppConstants.SEARCH_URL)
    fun searchMoviesUsingKeyword(@Query("api_key") apiKey: String, @Query("query") keyword: String, @Query("page") page: String
                                 , @Query("include_adult") includeAdult: String): Call<CurrentMoviesResponse>

    @GET(AppConstants.POPULAR_MOVIES_URL)
    fun searchPopularMovies(@Query("api_key") apiKey: String, @Query("language") language: String, @Query("page") page:String): Call<CurrentMoviesResponse>

    @GET(AppConstants.NOW_PLAYING_URL)
    fun searchCurrentMovies(@Query("api_key") apiKey: String, @Query("language") language: String, @Query("page") page:String): Call<CurrentMoviesResponse>

    @GET(AppConstants.MOVIES_DETAIL_URL)
    fun getMoviesDetail(@Query("api_key") apiKey: String, @Query("language") language: String): Call<CurrentMoviesResponse>

    @GET(AppConstants.TOP_RATED_MOVIES_URL)
    fun searchTopRatedMovies(@Query("api_key") apiKey: String, @Query("language") language: String, @Query("page") page:String): Call<CurrentMoviesResponse>

    @GET
    fun searchMoviesVideos(@Url url: String, @Query("api_key") apiKey:String): Call<MoviesVideoResult>
}