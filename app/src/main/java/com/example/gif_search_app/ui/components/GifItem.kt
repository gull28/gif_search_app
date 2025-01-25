package com.example.gif_search_app.ui.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

@Composable
fun GifItem(gifUrl: String, onClick: (() -> Unit)? = null, contentScale: ContentScale) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = gifUrl,
            contentDescription = null,
            imageLoader = imageLoader,
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).padding(6.dp).then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            ),
            contentScale = contentScale
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        modifier = Modifier
                            .size(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                is AsyncImagePainter.State.Error -> {
                    Text(
                        text = "Failed to load",
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                }
                else -> {
                    SubcomposeAsyncImageContent()
                }
            }
        }
    }
}