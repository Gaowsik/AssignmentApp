package com.example.assignmentapp.domain.repository

import com.example.assignmentapp.data.APIResource
import com.example.assignmentapp.data.Resource
import com.example.assignmentapp.domain.model.NewsItem

interface NewsRepository {
    suspend fun getTopHeadlines(
        country: String,
        category: String? = null,
        page: Int,
        pageSize: Int
    ): APIResource<List<NewsItem>>

    suspend fun searchNews(
        query: String,
        page: Int,
        pageSize: Int
    ): APIResource<List<NewsItem>>

    suspend fun getNewsByCategory(
        category: String,
        country: String,
        page: Int,
        pageSize: Int
    ): APIResource<List<NewsItem>>

    suspend fun getNewsByCountry(
        country: String,
        page: Int,
        pageSize: Int
    ): APIResource<List<NewsItem>>


    suspend fun addFavorite(newsItem: NewsItem): Resource<Unit>

    suspend fun removeFavorite(newsItem: NewsItem): Resource<Unit>

    suspend fun getAllFavorites(): Resource<List<NewsItem>>

    suspend fun isFavorite(url: String): Resource<Boolean>
}