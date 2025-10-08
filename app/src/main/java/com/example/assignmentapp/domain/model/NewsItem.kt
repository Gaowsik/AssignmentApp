package com.example.assignmentapp.domain.model

data class NewsItem(
    val title: String?,
    val author: String?,
    val description: String?,
    val imageUrl: String?,
    val publishedAt: String?,
    val url: String,
    val isFavorite: Boolean = false
)