package com.example.gif_search_app.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.gif_search_app.data.model.GifObject
import com.example.gif_search_app.data.paging.GifPagingSource
import com.example.gif_search_app.data.repo.GiphyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: GiphyRepository,
    private val apiKey: String
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _gifs = MutableStateFlow<PagingData<GifObject>>(PagingData.empty())
    val gifs: StateFlow<PagingData<GifObject>> get() = _gifs

    init {
        viewModelScope.launch {
            query
                .debounce(800)
                .distinctUntilChanged()
                .collectLatest { queryText ->
                    if (queryText.isBlank()) {
                        _gifs.value = PagingData.empty()
                    } else {
                        searchGif(queryText)
                    }
                }
        }
    }

    fun searchGif(query: String) {
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { GifPagingSource(repository.api, apiKey, query) }
        ).liveData
            .cachedIn(viewModelScope)
            .observeForever { pagingData ->
                _gifs.value = pagingData
            }
    }

    fun updateQuery(queryText: String) {
        _query.value = queryText
    }
}
