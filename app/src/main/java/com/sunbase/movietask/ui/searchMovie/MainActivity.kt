package com.sunbase.movietask.ui.searchMovie

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunbase.movietask.R
import com.sunbase.movietask.data.local.model.RecentSearch
import com.sunbase.movietask.databinding.ActivityMainBinding
import com.sunbase.movietask.ui.adapter.LoadStateAdapter
import com.sunbase.movietask.ui.adapter.MovieAdapter
import com.sunbase.movietask.ui.adapter.RecentSearchAdapter
import com.sunbase.movietask.util.Constants
import com.sunbase.movietask.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MovieViewModel by viewModels()
    private val movieAdapter: MovieAdapter by lazy { MovieAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setListeners()
        setupMovieAdapter()
        subscribeToData()
    }

    private fun init() {
        viewModel.getRecentSearches()
    }

    private fun setListeners() {
        binding.searchInputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(this)
                viewModel.search(binding.searchInputText.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
        binding.ivSwitchView.setOnClickListener {
            viewModel.toggleViewType()
        }
    }

    private fun subscribeToData() {
        viewModel.recentSearch.observe(this) {
            setupRecentSearch(it)
        }
        viewModel.viewType.observe(this) {
            movieAdapter.setViewType(it)
            binding.ivSwitchView.setImageDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    if (it == Constants.VIEW_GRID) R.drawable.ic_baseline_grid_view_24
                    else R.drawable.ic_outline_list_24
                )
            )
            binding.rvMovies.apply {
                layoutManager = GridLayoutManager(
                    this@MainActivity,
                    it
                )
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.movies.collectLatest {
                movieAdapter.submitData(it)
            }
        }
    }

    private fun setupMovieAdapter() {
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(this@MainActivity, Constants.VIEW_LIST)
            adapter = movieAdapter.withLoadStateFooter(
                LoadStateAdapter {
                    movieAdapter.retry()
                }
            )
        }
        movieAdapter.addLoadStateListener { loadStates: CombinedLoadStates ->
            if (loadStates.refresh is LoadState.Error) {
                //Error Handling
                binding.pbLoader.visibility = View.GONE
                binding.rvMovies.visibility = View.GONE
                binding.noDataLayout.tvNoRecords.text =
                    (loadStates.refresh as LoadState.Error).error.localizedMessage!!
                binding.noDataLayout.noItems.visibility = View.VISIBLE
            }
            if (loadStates.refresh is LoadState.Loading) {
                //Loading in progress
                binding.noDataLayout.noItems.visibility = View.GONE
                binding.rlRecentSearch.visibility = View.GONE
                binding.rvMovies.visibility = View.GONE
                binding.pbLoader.visibility = View.VISIBLE
            }
            if (loadStates.prepend is LoadState.NotLoading && loadStates.prepend.endOfPaginationReached) {
                binding.pbLoader.visibility = View.GONE
                binding.rvMovies.visibility = View.VISIBLE
            }
            if (loadStates.append is LoadState.NotLoading && loadStates.append.endOfPaginationReached) {
                // Loading finished
                binding.rvMovies.visibility = View.VISIBLE
                binding.rlRecentSearch.visibility = View.GONE
                binding.noDataLayout.noItems.visibility = View.GONE
            }
        }
    }

    private fun setupRecentSearch(searchItems: List<RecentSearch>) {
        searchItems.let { it ->
            if (it.isNotEmpty()) {
                val adapter = RecentSearchAdapter(it.map { it.search },
                    object : RecentSearchAdapter.RecentSearchInterface {
                        override fun onClick(item: String) {
                            hideKeyboard(this@MainActivity)
                            binding.searchInputText.setText(item)
                            binding.searchInputText.setSelection(binding.searchInputText.length())
                            viewModel.search(item)
                        }
                    })
                val layoutManager = LinearLayoutManager(this)
                binding.rvRecentSearches.layoutManager = layoutManager
                binding.rvRecentSearches.setHasFixedSize(true)
                binding.rvRecentSearches.adapter = adapter
                binding.rlRecentSearch.visibility = View.VISIBLE
                binding.noDataLayout.noItems.visibility = View.GONE
            } else {
                binding.noDataLayout.tvNoRecords.text = getString(R.string.no_recent_search)
                binding.noDataLayout.noItems.visibility = View.VISIBLE
                binding.rlRecentSearch.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        if (binding.searchInputText.text.toString().isNotEmpty()
            || binding.rvMovies.visibility == View.VISIBLE
        ) {
            binding.searchInputText.setText("")
            binding.rvMovies.scheduleLayoutAnimation()
            binding.rvMovies.visibility = View.GONE
            viewModel.getRecentSearches()
        } else
            super.onBackPressed()
    }

}