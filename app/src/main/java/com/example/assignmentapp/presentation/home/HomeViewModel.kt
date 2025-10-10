package com.example.assignmentapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignmentapp.data.APIResource
import com.example.assignmentapp.data.Resource
import com.example.assignmentapp.domain.model.NewsItem
import com.example.assignmentapp.domain.repository.NewsRepository
import com.example.assignmentapp.utils.AppConstants.DEFAULT_REQUEST_PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val newsRepository: NewsRepository
) : ViewModel() {

    private val _latestNews = MutableStateFlow<List<NewsItem>>(emptyList())
    val latestNews: StateFlow<List<NewsItem>> = _latestNews.asStateFlow()

    private val _newsFeed = MutableStateFlow<List<NewsItem>>(emptyList())
    val newsFeed: StateFlow<List<NewsItem>> = _newsFeed.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedNewsItem = MutableStateFlow<NewsItem?>(null)
    val selectedNewsItem: StateFlow<NewsItem?> = _selectedNewsItem.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isFavoriteSuccessful = MutableSharedFlow<Boolean>()
    val isFavoriteSuccessful = _isFavoriteSuccessful.asSharedFlow()

    private val _isFavoriteRemoved = MutableSharedFlow<Boolean>()
    val isFavoriteRemoved = _isFavoriteRemoved.asSharedFlow()


    private var currentPageFeed = 1
    private var currentPageLatest = 1
    private val pageSize = DEFAULT_REQUEST_PAGE_SIZE
    private var selectedCategory: String? = null
    private val defaultCountry = "us"



    fun fetchLatestNews(refresh: Boolean = false,topHeadlinesSize: Int = 5) {
        viewModelScope.launch {
            if (refresh) {
                currentPageLatest = 1
                _latestNews.value = emptyList()
            }

            _isLoading.value = true
            when (val result = newsRepository.getTopHeadlines(
                country = defaultCountry,
                page = currentPageLatest,
                pageSize = topHeadlinesSize
            )) {
                is APIResource.Success -> {
                    val list = if (refresh) result.value else _latestNews.value + result.value
                    _latestNews.value = list.take(topHeadlinesSize)
                    currentPageLatest++
                }

                is APIResource.Error -> _errorMessage.emit(result.errorBody.toString())
                APIResource.Loading -> TODO()
            }
            _isLoading.value = false
        }
    }

    fun fetchNewsFeed(refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh) {
                currentPageFeed = 1
                _newsFeed.value = emptyList()
            }

            _isLoading.value = true
            val response = if (selectedCategory != null) {
                newsRepository.getNewsByCategory(
                    category = selectedCategory!!,
                    country = defaultCountry,
                    page = currentPageFeed,
                    pageSize = pageSize
                )
            } else {
                newsRepository.getTopHeadlines(
                    country = defaultCountry,
                    page = currentPageFeed,
                    pageSize = pageSize
                )
            }

            when (response) {
                is APIResource.Success -> {
                    val newList = if (refresh) response.value else _newsFeed.value + response.value
                    _newsFeed.value = newList
                    currentPageFeed++
                }

                is APIResource.Error -> _errorMessage.emit(response.errorBody.toString())
                APIResource.Loading -> TODO()
            }

            _isLoading.value = false
        }
    }

    fun loadMoreFeed() = fetchNewsFeed()
    fun loadMoreLatestNews() = fetchLatestNews(topHeadlinesSize = DEFAULT_REQUEST_PAGE_SIZE)

    fun setCategory(category: String?) {
        selectedCategory = category
        fetchNewsFeed(refresh = true)
    }

    fun searchNews(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = newsRepository.searchNews(query, 1, pageSize)) {
                is APIResource.Success -> _newsFeed.value = result.value
                is APIResource.Error -> _errorMessage.emit(result.errorBody.toString())
                APIResource.Loading -> TODO()
            }
            _isLoading.value = false
        }
    }


    fun selectNewsItem(item: NewsItem) {
        _selectedNewsItem.value = item
    }

    fun addFavorite(newsItem: NewsItem) {
        viewModelScope.launch {
            when(val result =newsRepository.addFavorite(newsItem)){
                is Resource.Error -> _errorMessage.emit(result.exception.message.toString())
                Resource.Loading -> TODO()
                is Resource.Success<*> -> _isFavoriteSuccessful.emit(true)
            }
        }

    }

    fun removeFavorite(newsItem: NewsItem) {
        viewModelScope.launch {
            when(val result =newsRepository.removeFavorite(newsItem)){
                is Resource.Error -> _errorMessage.emit(result.exception.message.toString())
                Resource.Loading -> TODO()
                is Resource.Success<*> -> _isFavoriteRemoved.emit(true)
            }
        }

    }
}
