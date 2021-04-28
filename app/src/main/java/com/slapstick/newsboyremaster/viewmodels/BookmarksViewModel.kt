package com.slapstick.newsboyremaster.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.slapstick.newsboyremaster.data.NewsRepository
import com.slapstick.newsboyremaster.data.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val repository: NewsRepository
): ViewModel() {

    fun getSavedNews() = repository.getSavedArticles().asLiveData()

    fun deleteArticle(article: Article) = viewModelScope.launch { repository.delete(article) }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.insert(article.copy(isSaved = true))
    }
}