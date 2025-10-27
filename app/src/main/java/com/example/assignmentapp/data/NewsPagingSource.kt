package com.example.assignmentapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.assignmentapp.data.source.remote.NewsApiDataSource
import com.example.assignmentapp.domain.model.NewsItem
import com.example.assignmentapp.domain.repository.NewsRepository


@OptIn(ExperimentalPagingApi::class)
class NewsPagingSource(
    private val newsRepository: NewsRepository,
    private val selectedCategory: String?,
    private val country: String
) : PagingSource<Int, NewsItem>() {
    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> {
        return try {
            val position = params.key ?: 1
            val response =
                newsRepository.getTopHeadlines(
                    country,
                    selectedCategory,
                    position,
                    params.loadSize
                )

            if (response is APIResource.Success) {
                LoadResult.Page(
                    data = response.value,
                    prevKey = if (position == 1) null else (position - 1),
                    nextKey = if (response.value.isEmpty()) null else position + 1
                )
            } else {
                LoadResult.Error(throw Exception("No Response"))
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}
