package com.example.assignmentapp.domain.model

import com.example.assignmentapp.data.source.local.entity.FavoriteNewsEntity

data class NewsItem(
    val title: String?,
    val author: String?,
    val description: String?,
    val imageUrl: String?,
    val publishedAt: String?,
    val url: String,
    val isFavorite: Boolean = true
) {


    fun toFavoriteNewsEntity(): FavoriteNewsEntity {
        return FavoriteNewsEntity(
            url = this.url,
            title = this.title,
            author = this.author,
            description = this.description,
            imageUrl = this.imageUrl,
            publishedAt = this.publishedAt,
            isFavorite = this.isFavorite
        )
    }
}