package com.example.gif_search_app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.example.gif_search_app.data.api.GiphyApi
import com.example.gif_search_app.data.model.GifObject
import com.example.gif_search_app.data.model.GiphyResponse
import com.example.gif_search_app.data.paging.GifPagingSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PopularViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockApi: GiphyApi = mockk()
    private val apiKey = "test_api_key"

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadPopularGifs with successful result`() = runTest {
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
            mockApi.getTrendingGifs(
                apiKey = apiKey,
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
        )

        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = 2,
            placeholdersEnabled = false
        )

        val loadResult = pagingSource.load(loadParams)

        assertTrue(loadResult is PagingSource.LoadResult.Page)

        val pageResult = loadResult

        assertEquals(mockGifObjects, pageResult.data)
        assertEquals(2, pageResult.nextKey)
    }

    @Test
    fun `loadPopularGifs with empty result`() = runTest {
        coEvery {
            mockApi.getTrendingGifs(
                apiKey = "test_api_key",
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
        )

        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = 2,
            placeholdersEnabled = false
        )

        val loadResult = pagingSource.load(loadParams)


        assertTrue(loadResult is PagingSource.LoadResult.Page)

        assertEquals(emptyList(), loadResult.data)
        assertNull(loadResult.nextKey)
    }

    @Test
    fun `test paging source error handling`() = runTest {
        coEvery {
            mockApi.getTrendingGifs(
                apiKey = "test_api_key",
                limit = any(),
                offset = any()
            )
        } throws UnknownHostException("No internet")

        val pagingSource = GifPagingSource(
            api = mockApi,
            apiKey = apiKey,
        )

        val loadParams = PagingSource.LoadParams.Refresh(
            key = 0,
            loadSize = 2,
            placeholdersEnabled = false
        )

        val loadResult = pagingSource.load(loadParams)

        Assert.assertTrue(loadResult is PagingSource.LoadResult.Error)

        val errorResult = loadResult as PagingSource.LoadResult.Error

        Assert.assertTrue(errorResult.throwable is GifPagingSource.NoInternetException)
    }
}