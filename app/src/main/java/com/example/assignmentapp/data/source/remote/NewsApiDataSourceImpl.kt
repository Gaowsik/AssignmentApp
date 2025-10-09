package com.example.assignmentapp.data.source.remote

import com.example.assignmentapp.data.APIResource
import com.example.assignmentapp.data.BaseRepo
import com.example.assignmentapp.domain.model.NewsItem
import javax.inject.Inject

class NewsApiDataSourceImpl @Inject constructor(
    private val newsApi: NewsApi
) : NewsApiDataSource, BaseRepo() {
    override suspend fun getTopHeadlines(
        country: String,
        category: String?,
        page: Int,
        pageSize: Int
    ) = safeApiCall {
        newsApi.getTopHeadlines(country,category,page,pageSize).articles.map { it.toNewsItem() }
    }

    override suspend fun searchNews(
        query: String,
        page: Int,
        pageSize: Int
    )= safeApiCall {
        newsApi.searchNews(query,page,pageSize).articles.map { it.toNewsItem() }
    }

    override suspend fun getNewsByCategory(
        category: String,
        country: String,
        page: Int,
        pageSize: Int
    )= safeApiCall {
        newsApi.getNewsByCategory(category,country,page,pageSize).articles.map { it.toNewsItem() }
    }

    override suspend fun getNewsByCountry(
        country: String,
        page: Int,
        pageSize: Int
    )= safeApiCall {
        newsApi.getNewsByCountry(country,page,pageSize).articles.map { it.toNewsItem() }
    }
}