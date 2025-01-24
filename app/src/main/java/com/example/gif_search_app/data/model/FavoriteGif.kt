package com.example.gif_search_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteGifs")
data class FavoriteGif(
    @PrimaryKey val gifId: String
)
