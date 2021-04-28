package com.slapstick.newsboyremaster.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.slapstick.newsboyremaster.data.NewsRepository
import com.slapstick.newsboyremaster.data.models.Article
import com.slapstick.newsboyremaster.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    state: SavedStateHandle
): ViewModel() {

    private val _articles = MutableLiveData<PagingData<Article>>()
    val articles: LiveData<PagingData<Article>> = _articles

    val currentQuery: MutableLiveData<String> = state.getLiveData(KEY_CURRENT_QUERY, "")

    init {
        currentQuery.observeForever { newQuery ->
            log("observeForever called for query: $newQuery")
            getAllNews(newQuery)
        }
    }

    fun searchNewsPaging(query: String) {
        currentQuery.value = query
    }

    fun getAllNews(newQuery: String) = viewModelScope.launch {
        if (newQuery.isNotEmpty()) {
            repository.searchNews(newQuery).cachedIn(viewModelScope).collect {
                _articles.value = it
            }
        } else {
            repository.getNews().cachedIn(viewModelScope).collect {
                _articles.value = it
            }
        }
    }

    fun saveArticleClicked(article: Article) = viewModelScope.launch {
        repository.insert(article.copy(isSaved = true))
    }

    companion object {
        private const val KEY_CURRENT_QUERY = "key_current_query"
    }

}