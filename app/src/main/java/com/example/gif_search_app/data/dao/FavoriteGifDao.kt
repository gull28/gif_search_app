package com.example.gif_search_app.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gif_search_app.data.model.FavoriteGif

@Dao
interface FavoriteGifDao {

    @Insert
    suspend fun insertFavoriteGif(favoriteGif: FavoriteGif)

    @Query("SELECT * FROM favoriteGifs LIMIT :limit OFFSET :offset")
    suspend fun getFavoriteGifs(limit: Int, offset: Int): List<FavoriteGif>

    @Query("SELECT EXISTS(SELECT 1 FROM favoriteGifs WHERE gifId = :gifId)")
    suspend fun isFavorite(gifId: String): Boolean

    @Query("DELETE FROM favoriteGifs WHERE gifId = :gifId")
    suspend fun deleteFavoriteGif(gifId: String)
}
