import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.gif_search_app.data.viewmodel.GifViewModel
import com.example.gif_search_app.data.viewmodel.StickersViewModel
import com.example.gif_search_app.ui.components.CollageGrid
import com.example.gif_search_app.ui.components.ErrorDisplay

@Composable
fun StickersView(
    navController: NavController,
    stickersViewModel: StickersViewModel,
    gifViewModel: GifViewModel
) {
    val configuration = LocalConfiguration.current
    val errorMessage by stickersViewModel.errorMessage.collectAsState()
    val stickers = stickersViewModel.gifs.collectAsLazyPagingItems()

    val columns = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> 6
        else -> 2
    }

    stickers.loadState.refresh.let { loadState ->
        if (loadState is LoadState.Error) {
            ErrorDisplay(
                message = errorMessage ?: "Failed to load sticker gifs",
                onRetry = { stickersViewModel.retry() }
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
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Stickers",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.1.sp,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            CollageGrid(
                gifs = stickers,
                columns = columns,
                onLoadMore = {
                    if (stickers.loadState.append is LoadState.NotLoading) {
                        stickers.retry()
                    }
                },
                modifier = Modifier.weight(1f),
                onGifClick = { gif ->
                    gifViewModel.updateSelectedGif(gif)
                    navController.navigate("gif")
                }
            )
        }
    }
}