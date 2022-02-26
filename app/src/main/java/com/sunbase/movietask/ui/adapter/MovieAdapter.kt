package com.sunbase.movietask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sunbase.movietask.databinding.GridMovieItemBinding
import com.sunbase.movietask.databinding.RowMovieItemBinding
import com.sunbase.movietask.data.remote.model.Movie
import com.sunbase.movietask.util.Constants
import com.sunbase.movietask.util.loadImage

class MovieAdapter : PagingDataAdapter<Movie, RecyclerView.ViewHolder>(MOVIE_COMPARATOR) {

    private var viewType: Int = Constants.VIEW_LIST

    fun setViewType(viewType: Int) {
        this.viewType = viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.VIEW_LIST) {
            val binding = RowMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
            ListViewHolder(binding)
        } else {
            val binding = GridMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
            GridViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            if (holder is ListViewHolder) holder.bind(it)
            if (holder is GridViewHolder) holder.bind(it)
        }
    }

    class ListViewHolder(private val binding: RowMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.apply {
                movie.poster?.let { ivPoster.loadImage(it) }
                tvName.text = movie.title
                tvYear.text = movie.year
            }
        }
    }

    class GridViewHolder(private val binding: GridMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.apply {
                movie.poster?.let { ivPoster.loadImage(it) }
                tvName.text = movie.title
                tvYear.text = movie.year
            }
        }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem == newItem
        }
    }
}