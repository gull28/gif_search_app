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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StickersViewModel @Inject constructor(
    private val repository: GiphyRepository,
    private val apiKey: String
) : ViewModel() {

    private val _stickers = MutableStateFlow<PagingData<GifObject>>(PagingData.empty())
    val stickers: StateFlow<PagingData<GifObject>> get() = _stickers

    init {
        loadPopularStickers()
    }

    private fun loadPopularStickers() {
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { GifPagingSource(repository.api, apiKey, sticker = true) }
        ).liveData
            .cachedIn(viewModelScope)
            .observeForever { pagingData ->
                _stickers.value = pagingData
            }
    }
}
