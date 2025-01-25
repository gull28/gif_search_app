package com.example.gif_search_app.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.gif_search_app.data.viewmodel.GifViewModel
import com.example.gif_search_app.data.viewmodel.SearchViewModel
import com.example.gif_search_app.ui.components.CollageGrid
import com.example.gif_search_app.ui.components.ErrorDisplay

@Composable
fun SearchView(navController: NavController, viewModel: SearchViewModel = hiltViewModel(), gifViewModel: GifViewModel) {
    val query by viewModel.query.collectAsState()
    val pagingItems = viewModel.gifs.collectAsLazyPagingItems()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val configuration = LocalConfiguration.current

    val columns = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> 6
        else -> 2
    }

    pagingItems.loadState.refresh.let { loadState ->
        if (loadState is LoadState.Error) {
            ErrorDisplay(
                message = errorMessage ?: "Failed to search gifs",
                onRetry = { viewModel.retry() }
            )
            return
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Search",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.1.sp,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            CollageGrid(
                gifs = pagingItems,
                columns = columns,
                onLoadMore = {
                    if (pagingItems.loadState.append is LoadState.NotLoading) {

                        pagingItems.retry()
                    }
                },
                onGifClick = { gifObject ->
                    gifViewModel.updateSelectedGif(gifObject)

                    navController.navigate("gif")
                }
            )
        }

        TextField(
            value = query,
            onValueChange = {
                viewModel.updateQuery(it)
            },
            placeholder = { Text("Search") },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .padding(8.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}
