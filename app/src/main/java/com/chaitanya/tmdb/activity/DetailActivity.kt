package com.chaitanya.tmdb.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import com.bylancer.classified.bylancerclassified.webservices.RetrofitController
import com.chaitanya.tmdb.R
import com.chaitanya.tmdb.utils.*
import com.chaitanya.tmdb.webservice.currentmovies.videos.MoviesVideoResult
import com.gmail.samehadar.iosdialog.IOSDialog
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : YouTubeBaseActivity(), View.OnClickListener, YouTubePlayer.OnInitializedListener {
    private var mYouTubePlayer: YouTubePlayer? = null
    private val RECOVERY_DIALOG_REQUEST = 1
    private var iOSDialog: IOSDialog? = null
    private var movieId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slideActivityRightToLeft()
        setContentView(R.layout.activity_detail)
        iOSDialog = getProgressView()
        if (intent != null && intent.getBundleExtra(AppConstants.BUNDLE) != null) {
            val bundle = intent.getBundleExtra(AppConstants.BUNDLE)
            movieId = bundle.getString(AppConstants.MOVIE_ID)
            detail_activity_overview.text = bundle.getString(AppConstants.MOVIE_OVERVIEW)
            detail_activity_release_date.text = "Release date " + bundle.getString(AppConstants.MOVIE_RELEASE_DATE)
            showProgressView()
            detail_activity_title.text = bundle.getString(AppConstants.MOVIE_NAME)
            youtube_view.initialize(YouTubeConfig.YOUTUBE_DEVELOPER_KEY, this)
        } else {
            showToast("Something went wrong, please try again")
        }
    }

    private fun fetchVideoDetailData() {
        if (!movieId.isNullOrEmpty()) {
            RetrofitController.getMoviesVideos(String.format(AppConstants.MOVIES_VIDEO_URL, movieId), object :
                Callback<MoviesVideoResult> {
                override fun onFailure(call: Call<MoviesVideoResult>?, t: Throwable?) {
                    dismissProgressView()
                }

                override fun onResponse(call: Call<MoviesVideoResult>?, response: Response<MoviesVideoResult>?) {
                    if (response != null && response.isSuccessful) {
                        if (!isFinishing && mYouTubePlayer != null) {
                            val list = arrayListOf<String>()
                            if (response.body() != null && !response.body().results.isNullOrEmpty()) {
                                for (video in response.body().results!!) {
                                    list.add(video.key!!)
                                }
                                mYouTubePlayer!!.cueVideos(list)
                            }
                        }
                    } else {

                    }
                    dismissProgressView()
                }
            })
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.detail_back_image_view -> onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        slideActivityLeftToRight()
    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        if (!wasRestored) {
            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            mYouTubePlayer = player
            fetchVideoDetailData()
            // Hiding player controls
            mYouTubePlayer?.setPlayerStyle(PlayerStyle.DEFAULT);
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, errorReason: YouTubeInitializationResult?) {
        if (errorReason != null && errorReason!!.isUserRecoverableError) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            showToast("Unable to initialize player")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YouTubeConfig.YOUTUBE_DEVELOPER_KEY, this)
        }
    }

    private fun getYouTubePlayerProvider(): YouTubePlayer.Provider {
        return findViewById<YouTubePlayerView>(R.id.youtube_view)
    }

    private fun showProgressView() {
        if (iOSDialog != null) {
            iOSDialog!!.show()
        }
    }

    private fun dismissProgressView() {
        if (iOSDialog != null) {
            iOSDialog!!.dismiss()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        dismissProgressView()
    }
}
