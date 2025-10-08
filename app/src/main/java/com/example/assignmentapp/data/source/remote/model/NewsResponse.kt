package com.example.assignmentapp.data.source.remote.model

import com.example.assignmentapp.domain.model.NewsItem

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
) {
    fun toNewsItem(): List<NewsItem> {
        return articles.map { article ->
            NewsItem(
                title = article.title,
                author = article.author,
                description = article.description,
                imageUrl = article.urlToImage,
                publishedAt = article.publishedAt,
                url = article.url,
            )
        }
    }
}