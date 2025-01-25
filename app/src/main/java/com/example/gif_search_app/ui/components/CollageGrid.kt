package com.example.gif_search_app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.gif_search_app.data.model.GifObject

@Composable
fun CollageGrid(
    gifs: LazyPagingItems<GifObject>,
    onGifClick: (GifObject) -> Unit,
    columns: Int,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyStaggeredGridState()

    LaunchedEffect(gifs.loadState) {
        val appendState = gifs.loadState.append
        if (appendState is androidx.paging.LoadState.Loading) {
            onLoadMore()
        }
    }


    Box(modifier = modifier) {
        if (gifs.itemCount == 0 && gifs.loadState.refresh !is LoadState.Loading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "No gifs. Try to search for a gif!",
                    modifier = Modifier.size(128.dp),
                    tint = Color.Gray
                )
                Text(
                    text = "No GIFs Available",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(columns),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp
            ) {
                items(gifs.itemCount) { index ->
                    gifs[index]?.let { gif ->
                        GifItem(
                            gifUrl = gif.images.original.url,
                            onClick = { onGifClick.invoke(gif) },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
