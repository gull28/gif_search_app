package com.example.gif_search_app.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.gif_search_app.data.api.GiphyApi
import com.example.gif_search_app.data.model.GifObject
import com.example.gif_search_app.data.paging.GifPagingSource

class GiphyRepository(val api: GiphyApi, private val apiKey: String) {

    suspend fun getTrendingGifs(limit: Int, offset: Int) = api.getTrendingGifs(apiKey, limit, offset)

    suspend fun searchGifs(query: String, limit: Int, offset: Int, rating: String = "g", lang: String = "en") =
        api.searchGifs(apiKey, query, limit, offset, rating, lang)


    suspend fun getStickers(limit: Int, offset: Int) = api.getTrendingStickers(apiKey, limit, offset)


    fun getGifPagingSource(query: String?, sticker: Boolean = false): Pager<Int, GifObject> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { GifPagingSource(api, apiKey, query, sticker) }
        )
    }
}