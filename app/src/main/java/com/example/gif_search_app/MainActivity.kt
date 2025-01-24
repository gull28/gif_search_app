package com.example.gif_search_app

import ConnectivityObserver
import NetworkConnectivityObserver
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*
import com.example.gif_search_app.data.api.GiphyApi
import com.example.gif_search_app.data.api.GiphyApiService
import com.example.gif_search_app.data.viewmodel.GifViewModel
import com.example.gif_search_app.data.viewmodel.PopularViewModel
import com.example.gif_search_app.data.viewmodel.SearchViewModel
import com.example.gif_search_app.data.viewmodel.StickersViewModel
import com.example.gif_search_app.ui.components.BottomNavBar
import com.example.gif_search_app.ui.theme.Gif_search_appTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var giphyApi: GiphyApi

    @Inject
    lateinit var giphyApiService: GiphyApiService

    private val searchViewModel: SearchViewModel by viewModels()
    private val popularViewModel: PopularViewModel by viewModels()
    private val gifViewModel: GifViewModel by viewModels()
    private val stickersViewModel: StickersViewModel by viewModels()

    private lateinit var connectivityObserver: ConnectivityObserver

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        connectivityObserver = NetworkConnectivityObserver(applicationContext)

        
        giphyApiService.printApiKey()
        setContent {


            Gif_search_appTheme {
                BottomNavBar(searchViewModel, popularViewModel, gifViewModel, stickersViewModel, connectivityObserver)
            }
        }
    }
}
