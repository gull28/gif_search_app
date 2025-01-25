package com.example.gif_search_app.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gif_search_app.data.api.GiphyApi
import com.example.gif_search_app.data.model.GifObject
import com.example.gif_search_app.data.paging.GifPagingSource
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
    private val api: GiphyApi,
    private val apiKey: String
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _gifs = MutableStateFlow<PagingData<GifObject>>(PagingData.empty())
    val gifs: StateFlow<PagingData<GifObject>> = _gifs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var currentPagingSource: GifPagingSource? = null

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
        _isLoading.value = true
        _errorMessage.value = null

        val pager = Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                GifPagingSource(api, apiKey, query)
                    .also { currentPagingSource = it }
            }
        )

        viewModelScope.launch {
            try {
                pager.flow
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _gifs.value = pagingData
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.message ?: "An error occurred while searching"
            }
        }
    }

    fun updateQuery(queryText: String) {
        _query.value = queryText
    }

    fun retry() {
        currentPagingSource?.invalidate()
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}