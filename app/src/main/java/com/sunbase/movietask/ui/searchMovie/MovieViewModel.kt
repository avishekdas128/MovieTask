package com.sunbase.movietask.ui.searchMovie

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.sunbase.movietask.data.db.model.RecentSearch
import com.sunbase.movietask.data.repo.MovieRepository
import com.sunbase.movietask.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _recentSearch = MutableLiveData<List<RecentSearch>>()
    val recentSearch: LiveData<List<RecentSearch>> = _recentSearch

    private val _viewType = MutableLiveData(Constants.VIEW_LIST)
    val viewType: LiveData<Int> = _viewType

    private val searchQuery = MutableLiveData("")

    val movies = searchQuery
        .asFlow()
        .filter {
            it.isNotBlank()
        }
        .flatMapLatest {
            addRecentSearch(it)
            repository.searchMovie(it).cachedIn(viewModelScope)
        }


    fun search(query: String) {
        viewModelScope.launch {
            searchQuery.postValue(query)
        }
    }

    fun getRecentSearches() {
        viewModelScope.launch {
            val data = repository.fetchRecentSearch()
            _recentSearch.postValue(data)
        }
    }

    private fun addRecentSearch(search: String) {
        viewModelScope.launch {
            repository.addRecentSearch(search)
        }
    }

    fun toggleViewType() {
        if (viewType.value == Constants.VIEW_LIST) _viewType.postValue(Constants.VIEW_GRID)
        else _viewType.postValue(Constants.VIEW_LIST)
    }
}