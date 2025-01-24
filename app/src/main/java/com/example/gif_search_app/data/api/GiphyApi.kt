package com.example.gif_search_app.data.api

import com.example.gif_search_app.data.model.GifObject
import com.example.gif_search_app.data.model.GiphyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {
    @GET("v1/gifs/trending")
    suspend fun getTrendingGifs(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): GiphyResponse

    @GET("v1/gifs/search")
    suspend fun searchGifs(
        @Query("api_key") apiKey: String,
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("rating") rating: String = "g",
        @Query("lang") lang: String = "en",
        @Query("bundle") bundle: String = "messaging_non_clips"
    ): GiphyResponse


    @GET("v1/stickers/trending")
    suspend fun getTrendingStickers(
        @Query("api_key") apiKey: String = "API_KEY_123",
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("rating") rating: String = "g",
        @Query("bundle") bundle: String = "messaging_non_clips"
    ): GiphyResponse
}
