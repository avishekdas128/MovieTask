package com.sunbase.movietask.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunbase.movietask.databinding.RowLoaderStateBinding

class LoadStateAdapter(
    private val retryCallback: View.OnClickListener
) : LoadStateAdapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RecyclerView.ViewHolder {
        val binding = RowLoaderStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
        (holder as LoadStateViewHolder).bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: RowLoaderStateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.btnRetry.setOnClickListener { retryCallback }
            when (loadState) {
                is LoadState.Error -> {
                    binding.tvError.text = loadState.error.localizedMessage
                    binding.tvError.visibility = View.VISIBLE
                    binding.btnRetry.visibility = View.VISIBLE
                    binding.pbLoader.visibility = View.GONE
                }
                is LoadState.Loading -> {
                    binding.tvError.visibility = View.GONE
                    binding.btnRetry.visibility = View.GONE
                    binding.pbLoader.visibility = View.VISIBLE
                }
                else -> {
                    binding.tvError.visibility = View.GONE
                    binding.btnRetry.visibility = View.GONE
                    binding.pbLoader.visibility = View.GONE
                }
            }
        }
    }
}