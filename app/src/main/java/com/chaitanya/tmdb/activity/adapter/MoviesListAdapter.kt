package com.chaitanya.tmdb.activity.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chaitanya.tmdb.R
import com.chaitanya.tmdb.listeners.UserTapOnMovie
import com.chaitanya.tmdb.utils.AppConstants
import com.chaitanya.tmdb.utils.getImageWihUrl
import com.chaitanya.tmdb.webservice.currentmovies.CurrentMoviesData

class MoviesListAdapter(private val moviesList : ArrayList<CurrentMoviesData>, private val onUserTapOnMovie: UserTapOnMovie) :
    RecyclerView.Adapter<MoviesListAdapter.MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_list_adapter, parent, false)
        return MoviesViewHolder(view)
    }

    override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val moviesData = moviesList.get(position)
        holder.moviesNameTextView?.text = moviesData.title
        holder.moviesTypeTextView?.text = if (moviesData.adult != null && moviesData.adult!!) "A" else ""
        holder.moviesRatingTextView?.text = "Voted by ${moviesData.voteCount} users"
        holder.moviesAvgRatingTextView?.text = "Average voting " + moviesData.voteAverage
        Glide.with(holder.moviesImageView!!.context).load(getImageWihUrl(moviesData.backdropPath, AppConstants.IMAGE_SIZE_ORIGINAL)).into(holder.moviesImageView!!)
        holder.itemView.setOnClickListener() {
            if (onUserTapOnMovie != null) {
                onUserTapOnMovie.onUserTapOnMovies(moviesData)
            }
        }
    }


    class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var moviesImageView : ImageView? = null
        var moviesTypeTextView : TextView? = null
        var moviesNameTextView : TextView? = null
        var moviesRatingTextView : TextView? = null
        var moviesAvgRatingTextView : TextView? = null
        init {
            moviesImageView = itemView.findViewById(R.id.movie_image_view)
            moviesTypeTextView = itemView.findViewById(R.id.movie_type)
            moviesNameTextView = itemView.findViewById(R.id.movie_name)
            moviesRatingTextView = itemView.findViewById(R.id.movie_voting)
            moviesAvgRatingTextView = itemView.findViewById(R.id.movie_avg_rating)
        }
    }
}