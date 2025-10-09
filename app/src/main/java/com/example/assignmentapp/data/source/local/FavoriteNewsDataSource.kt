package com.example.assignmentapp.data.source.local

import com.example.assignmentapp.data.Resource
import com.example.assignmentapp.domain.model.NewsItem

interface FavoriteNewsDataSource {
    suspend fun addFavorite(newsItem: NewsItem): Resource<Unit>
    suspend fun removeFavorite(newsItem: NewsItem): Resource<Unit>
    suspend fun getAllFavorites(): Resource<List<NewsItem>>
    suspend fun isFavorite(url: String): Resource<Boolean>
    suspend fun getAllFavoriteUrls(): Resource<List<String>>
}