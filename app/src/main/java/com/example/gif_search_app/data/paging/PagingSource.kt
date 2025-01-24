package com.example.gif_search_app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gif_search_app.data.model.GifObject
import com.example.gif_search_app.data.api.GiphyApi

class GifPagingSource(
    private val api: GiphyApi,
    private val apiKey: String,
    private val query: String? = null,
    private val sticker: Boolean? = false,
) : PagingSource<Int, GifObject>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifObject> {
        val position = params.key ?: 0

        return try {
            val response = if (sticker == true) {
                api.getTrendingStickers(apiKey, params.loadSize, position)
            } else if(query.isNullOrEmpty()) {
                api.getTrendingGifs(apiKey, params.loadSize, position)

            } else {
                api.searchGifs(apiKey, query, params.loadSize, position)
            }

            val gifs = response.data

            LoadResult.Page(
                data = gifs,
                prevKey = if (position == 0) null else position - params.loadSize,
                nextKey = if (gifs.isEmpty()) null else position + params.loadSize
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GifObject>): Int? {
        return state.anchorPosition
    }
}


