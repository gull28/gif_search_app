package com.example.gif_search_app.data.api

import javax.inject.Inject

class GiphyApiService @Inject constructor(
    private val apiKey: String
) {

    fun getApiKey(): String {
        return apiKey
    }
}
