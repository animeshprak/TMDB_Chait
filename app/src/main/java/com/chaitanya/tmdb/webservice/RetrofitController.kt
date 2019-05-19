package com.bylancer.classified.bylancerclassified.webservices

import com.chaitanya.tmdb.utils.AppConstants
import com.chaitanya.tmdb.utils.AppConstants.Companion.BASE_URL
import com.chaitanya.tmdb.utils.getAPISecret
import com.chaitanya.tmdb.webservice.currentmovies.CurrentMoviesResponse
import com.chaitanya.tmdb.webservice.currentmovies.videos.MoviesVideoResult
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitController {
    companion object {
        private val mRetrofit: Retrofit = getInstance()
        private val webserviceApi = mRetrofit.create<WebServiceApiInterface>(WebServiceApiInterface::class.java!!)

        fun getInstance() : Retrofit {
            if (mRetrofit == null) {
                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                return Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }

            return mRetrofit
        }

        fun getCurrentMovies(pageNumber: String, currentMoviesCallBack: Callback<CurrentMoviesResponse>) {
            val call = webserviceApi.searchCurrentMovies(getAPISecret(), AppConstants.MOVIES_LANGUAGE, pageNumber)
            call.enqueue(currentMoviesCallBack)
        }

        fun searchMoviesWithKeyword(pageNumber: String, keyword: String, searchMoviesCallBack: Callback<CurrentMoviesResponse>) {
            val call = webserviceApi.searchMoviesUsingKeyword(getAPISecret(), keyword, pageNumber, "true")
            call.enqueue(searchMoviesCallBack)
        }

        fun getPopularMovies(pageNumber: String, currentMoviesCallBack: Callback<CurrentMoviesResponse>) {
            val call = webserviceApi.searchPopularMovies(getAPISecret(), AppConstants.MOVIES_LANGUAGE, pageNumber)
            call.enqueue(currentMoviesCallBack)
        }

        fun getTopRatedMovies(pageNumber: String, currentMoviesCallBack: Callback<CurrentMoviesResponse>) {
            val call = webserviceApi.searchTopRatedMovies(getAPISecret(), AppConstants.MOVIES_LANGUAGE, pageNumber)
            call.enqueue(currentMoviesCallBack)
        }

        fun getMoviesVideos(url: String, moviesVideoCallBack: Callback<MoviesVideoResult>) {
            val call = webserviceApi.searchMoviesVideos(url, getAPISecret())
            call.enqueue(moviesVideoCallBack)
        }
    }
}