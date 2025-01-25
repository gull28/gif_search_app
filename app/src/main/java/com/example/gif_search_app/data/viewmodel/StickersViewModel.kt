package com.example.gif_search_app.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gif_search_app.data.model.GifObject
import com.example.gif_search_app.data.paging.GifPagingSource
import com.example.gif_search_app.data.repo.GiphyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StickersViewModel @Inject constructor(
    private val repository: GiphyRepository,
    private val apiKey: String
) : ViewModel() {

    private val _gifs = MutableStateFlow<PagingData<GifObject>>(PagingData.empty())
    val gifs: StateFlow<PagingData<GifObject>> = _gifs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _errorType = MutableStateFlow<ErrorType>(ErrorType.None)
    val errorType: StateFlow<ErrorType> = _errorType.asStateFlow()

    private var currentPagingSource: GifPagingSource? = null

    sealed class ErrorType {
        object None : ErrorType()
        object NoInternet : ErrorType()
        object NetworkError : ErrorType()
        object ApiError : ErrorType()
        object Unknown : ErrorType()
    }

    init {
        loadPopularStickers()
    }

    fun loadPopularStickers() {
        _isLoading.value = true
        _errorMessage.value = null
        _errorType.value = ErrorType.None

        viewModelScope.launch {
            try {
                val pager = Pager(
                    config = PagingConfig(
                        pageSize = 10,
                        enablePlaceholders = false,
                        initialLoadSize = 10
                    ),
                    pagingSourceFactory = {
                        GifPagingSource(repository.api, apiKey, sticker = true)
                            .also { currentPagingSource = it }
                    }
                )

                pager.flow
                    .cachedIn(viewModelScope)
                    .distinctUntilChanged()
                    .catch { exception ->
                        when (exception) {
                            is GifPagingSource.NoInternetException -> {
                                _errorType.value = ErrorType.NoInternet
                                _errorMessage.value = "No internet connection"
                            }
                            is GifPagingSource.NetworkException -> {
                                _errorType.value = ErrorType.NetworkError
                                _errorMessage.value = "Network error: ${exception.message}"
                            }
                            is GifPagingSource.ApiException -> {
                                _errorType.value = ErrorType.ApiError
                                _errorMessage.value = "API error: ${exception.message}"
                            }
                            else -> {
                                _errorType.value = ErrorType.Unknown
                                _errorMessage.value = "An unexpected error occurred"
                            }
                        }
                        _isLoading.value = false
                    }
                    .collect { pagingData ->
                        _gifs.value = pagingData
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.message ?: "An unexpected error occurred while loading stickers"
                _errorType.value = ErrorType.Unknown
            }
        }
    }

    fun retry() {
        _errorMessage.value = null
        _errorType.value = ErrorType.None

        currentPagingSource?.invalidate()

        loadPopularStickers()
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
        _errorType.value = ErrorType.None
    }
}