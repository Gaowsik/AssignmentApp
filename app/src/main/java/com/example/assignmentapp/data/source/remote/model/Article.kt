package com.example.assignmentapp.data.source.remote.model

import com.example.assignmentapp.domain.model.NewsItem

data class Article(
    val source: Source?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
){
      fun toNewsItem(): NewsItem {
          return NewsItem(
              title = title,
              author = author,
              description = description,
              imageUrl = urlToImage,
              publishedAt = publishedAt,
              url = url,
          )
      }
}