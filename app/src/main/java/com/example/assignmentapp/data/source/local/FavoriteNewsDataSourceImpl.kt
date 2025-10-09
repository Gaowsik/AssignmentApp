package com.example.assignmentapp.data.source.local

import com.example.assignmentapp.data.BaseRepo
import com.example.assignmentapp.data.Resource
import com.example.assignmentapp.domain.model.NewsItem
import jakarta.inject.Inject

class FavoriteNewsDataSourceImpl @Inject constructor(
    private val dao: FavoriteNewsDao
) : FavoriteNewsDataSource, BaseRepo() {
    override suspend fun addFavorite(newsItem: NewsItem): Resource<Unit> = safeCall {
        dao.insertFavorite(newsItem.toFavoriteNewsEntity())
    }

    override suspend fun removeFavorite(newsItem: NewsItem): Resource<Unit> = safeCall {
        dao.deleteFavorite(newsItem.toFavoriteNewsEntity())
    }

    override suspend fun getAllFavorites(): Resource<List<NewsItem>> = safeCall {
        dao.getAllFavorites().map { it.toNewsItem() }
    }

    override suspend fun isFavorite(url: String): Resource<Boolean> = safeCall {
        dao.getFavoriteByUrl(url) != null
    }

    override suspend fun getAllFavoriteUrls() = safeCall {
        dao.getAllFavoriteUrls()
    }


}