package com.example.gif_search_app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gif_search_app.data.model.GifObject
import com.example.gif_search_app.data.api.GiphyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class GifPagingSource(
    private val api: GiphyApi,
    private val apiKey: String,
    private val query: String? = null,
    private val sticker: Boolean = false,
) : PagingSource<Int, GifObject>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifObject> {
        val position = params.key ?: 0

        return try {
            val response = withContext(Dispatchers.IO) {
                when {
                    sticker -> api.getTrendingStickers(apiKey, params.loadSize, position)
                    query.isNullOrEmpty() -> api.getTrendingGifs(apiKey, params.loadSize, position)
                    else -> api.searchGifs(apiKey, query, params.loadSize, position)
                }
            }

            val gifs = response.data

            LoadResult.Page(
                data = gifs,
                prevKey = if (position == 0) null else position - params.loadSize,
                nextKey = if (gifs.isEmpty()) null else position + params.loadSize
            )
        } catch (e: UnknownHostException) {
            LoadResult.Error(NoInternetException("No internet connection"))
        } catch (e: IOException) {
            LoadResult.Error(NetworkException("Network error occurred", e))
        } catch (e: HttpException) {
            LoadResult.Error(ApiException("API error: ${e.code()}", e))
        } catch (e: Exception) {
            LoadResult.Error(UnknownException("An unexpected error occurred", e))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GifObject>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    class NoInternetException(message: String) : Exception(message)
    class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)
    class ApiException(message: String, cause: Throwable? = null) : Exception(message, cause)
    class UnknownException(message: String, cause: Throwable? = null) : Exception(message, cause)
}