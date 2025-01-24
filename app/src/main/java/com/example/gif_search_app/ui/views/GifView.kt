package com.example.gif_search_app.ui.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gif_search_app.data.viewmodel.GifViewModel
import com.example.gif_search_app.ui.components.GifItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GifView(
    navController: NavController,
    gifViewModel: GifViewModel,
) {
    val gif = gifViewModel.selectedGif
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Back") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center
            ) {
                gif?.let {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GifItem(
                            gifUrl = it.images.original.url,
                            contentScale = ContentScale.Fit
                        )

                        Text(text = it.title, modifier = Modifier.padding(vertical = 16.dp))



                            Button(
                                onClick = { openGifInBrowser(context, it.images.original.url) },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.OpenInBrowser,
                                    contentDescription = "Open in browser",
                                    modifier = Modifier.padding(end = 8.dp)
                                )

                                Text(text = "Open in Browser")
                            }

                    }
                } ?: Text(
                    text = "Error opening GIF.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    )
}

fun openGifInBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}