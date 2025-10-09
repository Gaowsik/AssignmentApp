package com.example.assignmentapp.data.source.remote

import com.example.assignmentapp.data.APIResource
import com.example.assignmentapp.domain.model.NewsItem

interface NewsApiDataSource {

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
}