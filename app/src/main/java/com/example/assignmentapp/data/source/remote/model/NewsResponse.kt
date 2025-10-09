package com.example.assignmentapp.data.source.remote.model

import com.example.assignmentapp.domain.model.NewsItem

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)