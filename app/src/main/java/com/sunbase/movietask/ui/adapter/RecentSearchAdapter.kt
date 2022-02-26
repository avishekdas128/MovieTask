package com.sunbase.movietask.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunbase.movietask.R
import com.sunbase.movietask.databinding.RowRecentSearchBinding

class RecentSearchAdapter(private val mList: List<String>,
                          private val searchInterface: RecentSearchInterface
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface RecentSearchInterface {
        fun onClick(item: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_recent_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: RowRecentSearchBinding = RowRecentSearchBinding.bind(itemView)

        @SuppressLint("NotifyDataSetChanged")
        fun bind(item: String) {
            item.apply {
                binding.tvRecent.text = this
                binding.root.setOnClickListener { searchInterface.onClick(this) }
            }

        }

    }
}