package com.chaitanya.tmdb.activity

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import com.bylancer.classified.bylancerclassified.webservices.RetrofitController
import com.chaitanya.tmdb.R
import com.chaitanya.tmdb.activity.adapter.MoviesListAdapter
import com.chaitanya.tmdb.listeners.LazyMoviesLoading
import com.chaitanya.tmdb.listeners.UserTapOnMovie
import com.chaitanya.tmdb.utils.*
import com.chaitanya.tmdb.webservice.currentmovies.CurrentMoviesData
import com.chaitanya.tmdb.webservice.currentmovies.CurrentMoviesResponse
import com.chaitanya.tmdb.widget.CustomAlertDialog
import com.gmail.samehadar.iosdialog.IOSDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), Callback<CurrentMoviesResponse>, LazyMoviesLoading, View.OnClickListener,
    UserTapOnMovie {

    private var iOSDialog: IOSDialog? = null
    private var mMoviesPageNumber = 1
    private var isMoviesDataLoading = false
    var searchBySelectedType = 5
    private var SPAN_COUNT = 1
    private var NOW_PLAYING = 5
    private var POPULAR = 6
    private var TOP_RATED = 7
    private var BY_SEARCH = 8
    private val moviesList = arrayListOf<CurrentMoviesData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slideActivityRightToLeft()
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        if (resources.getBoolean(R.bool.isTablet)) {
            SPAN_COUNT = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                3
            } else {
                2
            }
        } else {
            SPAN_COUNT = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                2
            } else {
                1
            }
        }
        movies_recycler_view.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        //movies_recycler_view.layoutManager = LinearLayoutManager(this)
        movies_recycler_view.setHasFixedSize(false)
        movies_recycler_view.itemAnimator = DefaultItemAnimator()
        movies_recycler_view.addItemDecoration(GridSpacingItemDecoration(SPAN_COUNT, 10, false))
        iOSDialog = getProgressView()
        initializingRecyclerViewScrollListener()
        initializingDoneKeywordListener()
        getMoviesFromDatabase(mMoviesPageNumber, true)
    }

    private fun initializingDoneKeywordListener() {
        search_movies_edit_text.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                fetchMoviesByKeyword()
            }
            false
        }
    }

    private fun getMoviesFromDatabase(pageNumber: Int, isLoaderRequired: Boolean) {
        showProgressView(isLoaderRequired)
        if (isLoaderRequired) {
            moviesList.clear()
        }
        if (!isMoviesDataLoading) {
            isMoviesDataLoading = true
            RetrofitController.getCurrentMovies(pageNumber.toString(), this)
        }
    }

    private fun getMoviesBySearch(pageNumber: Int, isLoaderRequired: Boolean) {
        showProgressView(isLoaderRequired)
        if (isLoaderRequired) {
            moviesList.clear()
        }
        if (!isMoviesDataLoading) {
            isMoviesDataLoading = true
            RetrofitController.searchMoviesWithKeyword(pageNumber.toString(), search_movies_edit_text.text.toString(),this)
        }
    }

    private fun getPopularMovies(pageNumber: Int, isLoaderRequired: Boolean) {
        showProgressView(isLoaderRequired)
        if (isLoaderRequired) {
            moviesList.clear()
        }
        if (!isMoviesDataLoading) {
            isMoviesDataLoading = true
            RetrofitController.getPopularMovies(pageNumber.toString(), this)
        }
    }

    private fun getTopRatedMovies(pageNumber: Int, isLoaderRequired: Boolean) {
        showProgressView(isLoaderRequired)
        if (isLoaderRequired) {
            moviesList.clear()
        }
        if (!isMoviesDataLoading) {
            isMoviesDataLoading = true
            RetrofitController.getTopRatedMovies(pageNumber.toString(), this)
        }
    }

    private fun showProgressView(isLoaderRequired: Boolean) {
        if (iOSDialog != null && isLoaderRequired) {
            iOSDialog!!.show()
            if (movies_recycler_view != null && movies_recycler_view.adapter != null) {
                movies_recycler_view.adapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun dismissProgressView() {
        if (iOSDialog != null) {
            iOSDialog!!.dismiss()
        }
        isMoviesDataLoading = false
    }

    override fun onFailure(call: Call<CurrentMoviesResponse>?, t: Throwable?) {
        dismissProgressView()
        showErrorMessage()
    }

    override fun onResponse(call: Call<CurrentMoviesResponse>?, response: Response<CurrentMoviesResponse>?) {
        if (response != null && response.isSuccessful && !isFinishing ) {
            if (response.body() != null) {
                response.body().results?.let { moviesList.addAll(it) }
                if (moviesList.isNullOrEmpty()) {
                    showToast("Please refine your search as we are unable to find any movies related to your search")
                } else  {
                    val adapter = MoviesListAdapter(moviesList, this)
                    if (movies_recycler_view != null) {
                        if (movies_recycler_view.adapter != null) {
                            movies_recycler_view.adapter!!.notifyDataSetChanged()
                        } else {
                            movies_recycler_view.adapter = adapter
                        }
                    }
                }
            }
        } else {
            showErrorMessage()
        }
        dismissProgressView()
    }

    override fun onMoviesLoadingRequired(currentVisibleItem: Int) {
        dismissProgressView()
        showErrorMessage()
    }

    private fun initializingRecyclerViewScrollListener() {
        movies_recycler_view.initScrollListener(object : LazyMoviesLoading {
            override fun onMoviesLoadingRequired(currentVisibleItem: Int) {
                if (moviesList != null) {
                    val itemSizeForLazyLoading = moviesList.size / 2
                    if (!isMoviesDataLoading && currentVisibleItem >= itemSizeForLazyLoading) {
                        mMoviesPageNumber += 1
                        when (searchBySelectedType) {
                            TOP_RATED -> {
                                getTopRatedMovies(mMoviesPageNumber, false)
                            }
                            POPULAR -> {
                                getPopularMovies(mMoviesPageNumber, false)
                            }
                            NOW_PLAYING -> {
                                getMoviesFromDatabase(mMoviesPageNumber, false)
                            }
                            BY_SEARCH -> {
                                getMoviesBySearch(mMoviesPageNumber, false)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun showErrorMessage() {
        showToast(getString(R.string.internet_issue))
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.search_imageView) {
            fetchMoviesByKeyword()
        } else if (view?.id == R.id.search_movies_by_type_fab) {
            showSortingAlertDialog()
        }
    }

    private fun fetchMoviesByKeyword() {
        if (!search_movies_edit_text.text.isNullOrEmpty()) {
            hideKeyboard()
            searchBySelectedType = BY_SEARCH
            mMoviesPageNumber = 1
            getMoviesBySearch(mMoviesPageNumber, true)
        } else {
            showToast("Please enter any keyword to search")
        }
    }

    private fun showSortingAlertDialog() {
        val sortingDialog = CustomAlertDialog(this, R.style.custom_filter_dialog)
        sortingDialog.setContentView(R.layout.movies_options_dialog)
        sortingDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        sortingDialog.getWindow().setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))
        sortingDialog.setCanceledOnTouchOutside(true)
        sortingDialog.show()

        val dialogTitle = sortingDialog.findViewById(R.id.options_title_text_view) as AppCompatTextView

        val nowPlayingTextView = sortingDialog.findViewById(R.id.sorting_new_to_old_text_view) as AppCompatTextView
        val popularMoviesTextView = sortingDialog.findViewById(R.id.sorting_old_to_to_text_view) as AppCompatTextView
        val topRatedMoviesTextView = sortingDialog.findViewById(R.id.sorting_price_high_to_low_text_view) as AppCompatTextView

        val nowPlayingLayout = sortingDialog.findViewById(R.id.sorting_new_to_old_layout) as ConstraintLayout
        val popularMoviesLayout = sortingDialog.findViewById(R.id.sorting_old_to_new_layout) as ConstraintLayout
        val topRatedMoviesLayout = sortingDialog.findViewById(R.id.sorting_price_high_to_low_layout) as ConstraintLayout

        dialogTitle.text = getString(R.string.search_movies_by)
        nowPlayingTextView.text = getString(R.string.now_playing)
        popularMoviesTextView.text = getString(R.string.popular)
        topRatedMoviesTextView.text = getString(R.string.top_rated)

        if (searchBySelectedType == 5) {
            nowPlayingTextView.setTypeface(nowPlayingTextView.typeface, Typeface.BOLD)
            popularMoviesTextView.setTypeface(popularMoviesTextView.typeface, Typeface.NORMAL)
            topRatedMoviesTextView.setTypeface(topRatedMoviesTextView.typeface, Typeface.NORMAL)

            nowPlayingLayout.setBackgroundColor(resources.getColor(R.color.gray))
            popularMoviesLayout.setBackgroundColor(resources.getColor(R.color.white_background))
            topRatedMoviesLayout.setBackgroundColor(resources.getColor(R.color.white_background))
        } else if (searchBySelectedType == 6){
            nowPlayingTextView.setTypeface(nowPlayingTextView.typeface, Typeface.NORMAL)
            popularMoviesTextView.setTypeface(popularMoviesTextView.typeface, Typeface.BOLD)
            topRatedMoviesTextView.setTypeface(topRatedMoviesTextView.typeface, Typeface.NORMAL)

            nowPlayingLayout.setBackgroundColor(resources.getColor(R.color.white_background))
            popularMoviesLayout.setBackgroundColor(resources.getColor(R.color.gray))
            topRatedMoviesLayout.setBackgroundColor(resources.getColor(R.color.white_background))
        } else if (searchBySelectedType == 7) {
            nowPlayingTextView.setTypeface(nowPlayingTextView.typeface, Typeface.NORMAL)
            popularMoviesTextView.setTypeface(popularMoviesTextView.typeface, Typeface.NORMAL)
            topRatedMoviesTextView.setTypeface(topRatedMoviesTextView.typeface, Typeface.BOLD)

            nowPlayingLayout.setBackgroundColor(resources.getColor(R.color.white_background))
            popularMoviesLayout.setBackgroundColor(resources.getColor(R.color.white_background))
            topRatedMoviesLayout.setBackgroundColor(resources.getColor(R.color.gray))
        } else {
            nowPlayingTextView.setTypeface(nowPlayingTextView.typeface, Typeface.NORMAL)
            popularMoviesTextView.setTypeface(popularMoviesTextView.typeface, Typeface.NORMAL)
            topRatedMoviesTextView.setTypeface(topRatedMoviesTextView.typeface, Typeface.NORMAL)

            nowPlayingLayout.setBackgroundColor(resources.getColor(R.color.white_background))
            popularMoviesLayout.setBackgroundColor(resources.getColor(R.color.white_background))
            topRatedMoviesLayout.setBackgroundColor(resources.getColor(R.color.white_background))
        }

        nowPlayingLayout.setOnClickListener() {
            if (searchBySelectedType != NOW_PLAYING) {
                searchBySelectedType = NOW_PLAYING
                getMoviesFromDatabase(mMoviesPageNumber, true)
            }
            sortingDialog.dismiss()
        }

        popularMoviesLayout.setOnClickListener() {
            if (searchBySelectedType != POPULAR) {
                searchBySelectedType = POPULAR
                getPopularMovies(mMoviesPageNumber, true)
            }
            sortingDialog.dismiss()
        }

        topRatedMoviesLayout.setOnClickListener() {
            if (searchBySelectedType != TOP_RATED) {
                searchBySelectedType = TOP_RATED
                getTopRatedMovies(mMoviesPageNumber, true)
            }
            sortingDialog.dismiss()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            SPAN_COUNT = if (resources.getBoolean(R.bool.isTablet)) {
                3
            } else {
                2
            }
            movies_recycler_view.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            SPAN_COUNT = if (resources.getBoolean(R.bool.isTablet)) {
                2
            } else {
                1
            }
            movies_recycler_view.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        }

        if (movies_recycler_view != null && movies_recycler_view.adapter != null) {
            movies_recycler_view.adapter!!.notifyDataSetChanged()
        }
    }

    override fun onUserTapOnMovies(currentMoviesData: CurrentMoviesData) {
        val detailIntent = Intent(this, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString(AppConstants.MOVIE_ID, currentMoviesData.id.toString())
        bundle.putString(AppConstants.MOVIE_NAME, currentMoviesData.originalTitle)
        bundle.putString(AppConstants.MOVIE_NAME, currentMoviesData.originalTitle)
        bundle.putString(AppConstants.MOVIE_OVERVIEW, currentMoviesData.overview)
        bundle.putString(AppConstants.MOVIE_RELEASE_DATE, currentMoviesData.releaseDate)
        detailIntent.putExtra(AppConstants.BUNDLE, bundle)
        startActivity(detailIntent)
    }
}
