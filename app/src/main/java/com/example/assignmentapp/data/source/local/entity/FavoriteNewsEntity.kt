package com.example.assignmentapp.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.assignmentapp.domain.model.NewsItem

@Entity(tableName = "favorite_news")
data class FavoriteNewsEntity(
    @PrimaryKey
    val url: String,
    val title: String?,
    val author: String?,
    val description: String?,
    val imageUrl: String?,
    val publishedAt: String?,
    val isFavorite: Boolean = true
) {
    fun toNewsItem(): NewsItem = NewsItem(
        title = title,
        author = author,
        description = description,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        url = url
    )
}