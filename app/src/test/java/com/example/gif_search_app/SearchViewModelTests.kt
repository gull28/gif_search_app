package com.example.gif_search_app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.example.gif_search_app.data.api.GiphyApi
import com.example.gif_search_app.data.model.GifObject
import com.example.gif_search_app.data.model.GiphyResponse
import com.example.gif_search_app.data.paging.GifPagingSource
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import java.net.UnknownHostException

import kotlin.test.assertEquals

class GifPagingSourceTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockApi: GiphyApi = mockk()
    private val apiKey = "test_api_key"

    @Test
    fun `test paging source load with search query`() = runTest {
        val mockGifObjects = listOf(
            GifObject(
                id = "test1",
                url = "test_url1",
                title = "Test GIF 1",
                images = mockk()
            ),
            GifObject(
                id = "test2",
                url = "test_url2",
                title = "Test GIF 2",
                images = mockk()
            )
        )

        coEvery {
            mockApi.searchGifs(
                apiKey = "test_api_key",
                query = "test",
                limit = any(),
                offset = any()
            )
        } returns GiphyResponse(
            data = mockGifObjects,
            pagination = mockk(),
            meta = mockk()
        )

        val pagingSource = GifPagingSource(
            api = mockApi,
            apiKey = apiKey,
            query = "test"
        )

        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = 2,
            placeholdersEnabled = false
        )

        val loadResult = pagingSource.load(loadParams)

        assertTrue(loadResult is PagingSource.LoadResult.Page)

        val pageResult = loadResult as PagingSource.LoadResult.Page

        assertEquals(mockGifObjects, pageResult.data)
        assertEquals(2, pageResult.nextKey)
    }

    @Test
    fun `test paging source load with empty result`() = runTest {
        coEvery {
            mockApi.searchGifs(
                apiKey = "test_api_key",
                query = "nonexistent",
                limit = any(),
                offset = any()
            )
        } returns GiphyResponse(
            data = emptyList(),
            pagination = mockk(),
            meta = mockk()
        )

        val pagingSource = GifPagingSource(
            api = mockApi,
            apiKey = apiKey,
            query = "nonexistent"
        )

        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = 2,
            placeholdersEnabled = false
        )

        val loadResult = pagingSource.load(loadParams)

        assertTrue(loadResult is PagingSource.LoadResult.Page)

        val pageResult = loadResult as PagingSource.LoadResult.Page

        assertEquals(emptyList(), pageResult.data)
        assertNull(pageResult.nextKey)
    }

    @Test
    fun `test paging source error handling`() = runTest {
        coEvery {
            mockApi.searchGifs(
                apiKey = "test_api_key",
                query = "error",
                limit = any(),
                offset = any()
            )
        } throws UnknownHostException("No internet")

        val pagingSource = GifPagingSource(
            api = mockApi,
            apiKey = apiKey,
            query = "error"
        )

        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = 2,
            placeholdersEnabled = false
        )

        val loadResult = pagingSource.load(loadParams)

        assertTrue(loadResult is PagingSource.LoadResult.Error)

        val errorResult = loadResult as PagingSource.LoadResult.Error

        assertTrue(errorResult.throwable is GifPagingSource.NoInternetException)
    }
}