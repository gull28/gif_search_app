package com.example.gif_search_app.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.gif_search_app.data.viewmodel.GifViewModel
import com.example.gif_search_app.data.viewmodel.PopularViewModel
import com.example.gif_search_app.ui.components.CollageGrid

@Composable
fun PopularView(navController: NavController, viewModel: PopularViewModel = hiltViewModel(), gifViewModel: GifViewModel = hiltViewModel()) {
    val configuration = LocalConfiguration.current

    val columns = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> 6
        else -> 2
    }
    val gifs = viewModel.gifs.collectAsLazyPagingItems()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {

            Text(
                text = "Popular",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.1.sp,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            CollageGrid(
                gifs = gifs,
                columns = columns,
                onLoadMore = {
                    if (gifs.loadState.append is LoadState.NotLoading) {

                        gifs.retry()
                    }
                },
                modifier = Modifier.weight(1f),
                onGifClick = { it
                    gifViewModel.updateSelectedGif(it)
                    navController.navigate("gif")
                }
            )
        }
    }
}

