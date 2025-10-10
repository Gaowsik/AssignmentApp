package com.example.assignmentapp.data.repository

import com.example.assignmentapp.data.APIResource
import com.example.assignmentapp.data.Resource
import com.example.assignmentapp.data.source.local.FavoriteNewsDataSource
import com.example.assignmentapp.data.source.remote.NewsApiDataSource
import com.example.assignmentapp.domain.model.NewsItem
import com.example.assignmentapp.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiDataSource: NewsApiDataSource,
    private val favoriteNewsDataSource: FavoriteNewsDataSource
) : NewsRepository {
    override suspend fun getTopHeadlines(
        country: String,
        category: String?,
        page: Int,
        pageSize: Int
    ): APIResource<List<NewsItem>> {
        val result = newsApiDataSource.getTopHeadlines(country, category, page, pageSize)
        return mergeWithFavorites(result)
    }

    override suspend fun searchNews(
        query: String,
        page: Int,
        pageSize: Int
    ): APIResource<List<NewsItem>> {
        val result = newsApiDataSource.searchNews(query, page, pageSize)
        return mergeWithFavorites(result)
    }

    override suspend fun getNewsByCategory(
        category: String,
        country: String,
        page: Int,
        pageSize: Int
    ): APIResource<List<NewsItem>> {
        val result = newsApiDataSource.getNewsByCategory(category, country, page, pageSize)
        return mergeWithFavorites(result)
    }

    override suspend fun getNewsByCountry(
        country: String,
        page: Int,
        pageSize: Int
    ): APIResource<List<NewsItem>> {
        val result = newsApiDataSource.getNewsByCountry(country, page, pageSize)
        return mergeWithFavorites(result)
    }

    override suspend fun addFavorite(newsItem: NewsItem) =
        favoriteNewsDataSource.addFavorite(newsItem)

    override suspend fun removeFavorite(newsItem: NewsItem) =
        favoriteNewsDataSource.removeFavorite(newsItem)

    override suspend fun getAllFavorites() = favoriteNewsDataSource.getAllFavorites()

    override suspend fun isFavorite(url: String) = favoriteNewsDataSource.isFavorite(url)


    private suspend fun mergeWithFavorites(
        result: APIResource<List<NewsItem>>
    ): APIResource<List<NewsItem>> {
        return when (result) {
            is APIResource.Success -> {
                val newsList = result.value

                when (val favoritesResult = favoriteNewsDataSource.getAllFavoriteUrls()) {
                    is Resource.Success -> {
                        val favSet = favoritesResult.value.toSet()
                        APIResource.Success(
                            newsList.map { it.copy(isFavorite = it.url in favSet) }
                        )
                    }

                    is Resource.Error -> {
                        APIResource.Success(newsList)
                    }

                    is Resource.Loading -> {
                        APIResource.Loading
                    }
                }
            }

            is APIResource.Error -> APIResource.Error(
                isNetworkError = result.isNetworkError,
                errorCode = result.errorCode,
                errorBody = result.errorBody
            )

            is APIResource.Loading -> APIResource.Loading
        }
    }




}