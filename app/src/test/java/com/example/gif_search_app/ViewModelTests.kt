package com.example.gif_search_app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import androidx.paging.map
import com.example.gif_search_app.data.model.GifObject
import com.example.gif_search_app.data.paging.GifPagingSource
import com.example.gif_search_app.data.viewmodel.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchViewModel
    private val mockPagingSource: GifPagingSource = mockk()
    private val apiKey = "test_api_key"

    @Before
    fun setUp() {
        // Set the Main dispatcher to a test dispatcher
        Dispatchers.setMain(Dispatchers.Unconfined)

        viewModel = SearchViewModel(mockk(), apiKey)
    }

    @Test
    fun `test debounce behavior in searchGif`() = runTest {
        coEvery {
            mockPagingSource.load(any<PagingSource.LoadParams<Int>>())
        } returns PagingSource.LoadResult.Page(
            data = listOf(GifObject(id = "axe", url = "axe", title = "axe", images = mockk())),
            prevKey = null,
            nextKey = 2
        )

        val collectedItems = mutableListOf<GifObject>()

        viewModel.updateQuery("axe")
        viewModel.updateQuery("axe")

        advanceTimeBy(800L)

        val job = launch {
            viewModel.gifs.collect { pagingData ->
                pagingData.map { collectedItems.add(it) }
            }
        }

        assertEquals(0, collectedItems.size)

        job.cancel()
    }
}
