package com.chaitanya.tmdb.utils

import android.app.Activity
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.chaitanya.tmdb.R
import com.chaitanya.tmdb.listeners.LazyMoviesLoading
import com.gmail.samehadar.iosdialog.IOSDialog
import java.nio.charset.StandardCharsets

class AppExtensions {}

fun getAPISecret() = String(Base64.decode(AppConstants.API_SECRET, Base64.DEFAULT), StandardCharsets.UTF_8)

fun getAccessTokenAuth() = AppConstants.API_ACCESS_TOKEN_AUTH

fun getImageWihUrl(imagePath: String?, imageSize: String) = String.format(AppConstants.IMAGE_BASE_URL, imageSize, imagePath)

fun Context.getProgressView() : IOSDialog {
    val dialog = IOSDialog.Builder(this)
        .setOnCancelListener {

        }
        .setDimAmount(3f)
        .setSpinnerColorRes(android.R.color.white)
        .setMessageColorRes(android.R.color.white)
        // .setTitle(R.string.standard_title)
        .setTitleColorRes(android.R.color.white)
        .setMessageContent("Loading...")
        .setCancelable(false)
        .setMessageContentGravity(Gravity.END)
        .build()
    return dialog
}

fun RecyclerView.initScrollListener(onMoviesLoading: LazyMoviesLoading) {
    addOnScrollListener( object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx:Int, dy:Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) { // dy to check it is scrolling down
                var linearLayoutManager = recyclerView.getLayoutManager() as LinearLayoutManager
                if (onMoviesLoading != null && linearLayoutManager != null) {
                    onMoviesLoading.onMoviesLoadingRequired(linearLayoutManager.findLastCompletelyVisibleItemPosition())
                }
            }
        }
    })
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Activity.hideKeyboard() {
    if(!this.isFinishing) {
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = this.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.slideActivityLeftToRight() {
    this.overridePendingTransition(R.anim.left_to_right_start, R.anim.right_to_left_end)
}

fun Activity.slideActivityRightToLeft() {
    this.overridePendingTransition(R.anim.right_to_left_start, R.anim.left_to_right_end)
}