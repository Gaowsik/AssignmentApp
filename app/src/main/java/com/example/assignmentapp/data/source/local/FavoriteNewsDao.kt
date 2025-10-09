package com.example.assignmentapp.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.assignmentapp.data.source.local.entity.FavoriteNewsEntity


@Dao
interface FavoriteNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(news: FavoriteNewsEntity)

    @Delete
    suspend fun deleteFavorite(news: FavoriteNewsEntity)

    @Query("SELECT * FROM favorite_news")
    suspend fun getAllFavorites(): List<FavoriteNewsEntity>

    @Query("SELECT * FROM favorite_news WHERE url = :url LIMIT 1")
    suspend fun getFavoriteByUrl(url: String): FavoriteNewsEntity?

    @Query("SELECT url FROM favorite_news WHERE url IN (:urls)")
    suspend fun getFavoriteUrls(urls: List<String>): List<String>
}