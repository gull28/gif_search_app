package com.example.gif_search_app.data.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gif_search_app.data.model.GifObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GifViewModel @Inject constructor() : ViewModel() {

    var selectedGif by mutableStateOf<GifObject?>(null)
        private set

    fun updateSelectedGif(gif: GifObject) {
        selectedGif = gif
    }
}
