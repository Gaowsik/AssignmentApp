package com.example.assignmentapp.presentation.favouriteNews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignmentapp.data.Resource
import com.example.assignmentapp.domain.model.NewsItem
import com.example.assignmentapp.domain.repository.NewsRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    val newsRepository: NewsRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _favoriteNews = MutableStateFlow<List<NewsItem>>(emptyList())
    val favoriteNews: StateFlow<List<NewsItem>> = _favoriteNews.asStateFlow()


    fun fetchFavoriteNews() {
        viewModelScope.launch {
            _isLoading.value = true
            val response = newsRepository.getAllFavorites()
            when (response) {
                is Resource.Error -> {
                    _errorMessage.emit(response.exception.message.toString())
                    _isLoading.value = false
                }

                Resource.Loading -> TODO()
                is Resource.Success<List<NewsItem>> -> {
                    _favoriteNews.value = response.value
                    _isLoading.value = false
                }
            }

        }
    }


}