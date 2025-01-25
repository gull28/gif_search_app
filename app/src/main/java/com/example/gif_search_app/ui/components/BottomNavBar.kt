package com.example.gif_search_app.ui.components

import ConnectivityObserver
import StickersView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gif_search_app.data.viewmodel.GifViewModel
import com.example.gif_search_app.data.viewmodel.PopularViewModel
import com.example.gif_search_app.data.viewmodel.SearchViewModel
import com.example.gif_search_app.data.viewmodel.StickersViewModel
import com.example.gif_search_app.ui.views.GifView
import com.example.gif_search_app.ui.views.PopularView
import com.example.gif_search_app.ui.views.SearchView

@Composable
fun BottomNavBar(searchViewModel: SearchViewModel, popularViewModel: PopularViewModel, gifViewModel: GifViewModel, stickersViewModel: StickersViewModel, connectivityObserver: ConnectivityObserver ) {
    val navController = rememberNavController()
    val status by connectivityObserver.observe().collectAsState(initial = ConnectivityObserver.Status.Unavailable)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("popular") },
                    label = { Text("Popular") },
                    icon = { Icon(Icons.Filled.Whatshot, contentDescription = "Popular") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("search") },
                    label = { Text("Search") },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },

                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("stickersView") },
                    label = { Text("Favorites") },
                    icon = { Icon(Icons.AutoMirrored.Filled.Label, contentDescription = "Favorites") }
                )
            }
        }
    ) {
        if(status != ConnectivityObserver.Status.Available) {
            NoInternetConnection()
        }else {
            NavHost(
                navController = navController,
                startDestination = "search",
                modifier = Modifier.padding(it)
            ) {
                composable("popular") {
                    PopularView(navController, popularViewModel, gifViewModel)
                }
                composable("search") {
                    SearchView(navController, searchViewModel, gifViewModel)
                }
                composable("stickersView") {
                    StickersView(navController = navController, stickersViewModel, gifViewModel )
                }
                composable("gif") {
                    GifView(navController, gifViewModel)
                }
            }
        }
    }
}