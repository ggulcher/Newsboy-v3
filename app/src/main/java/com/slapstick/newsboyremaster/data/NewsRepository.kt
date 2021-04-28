package com.slapstick.newsboyremaster.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.slapstick.newsboyremaster.data.models.Article
import com.slapstick.newsboyremaster.paging.NewsPagingSource
import com.slapstick.newsboyremaster.paging.safeCall
import com.slapstick.newsboyremaster.util.Constants.Companion.MAX_REQUEST
import com.slapstick.newsboyremaster.util.Constants.Companion.PAGE_REQUEST
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val api: NewsApi,
    private val dao: NewsDao
) {

    fun getNews(): Flow<PagingData<Article>> {
        return Pager(
            config = generatePagingConfig(),
            pagingSourceFactory = {
                NewsPagingSource { position, loadSize ->
                    safeCall { api.getNews(position, loadSize) }
                }
            }
        ).flow
    }

    fun searchNews(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = generatePagingConfig(),
            pagingSourceFactory = {
                NewsPagingSource { position, size ->
                    safeCall { api.searchNews(query, position, size) }
                }
            }
        ).flow
    }

    private fun generatePagingConfig() = PagingConfig(
        maxSize = MAX_REQUEST,
        pageSize = PAGE_REQUEST,
        enablePlaceholders = false // displaying placeholder for object not loaded yet.
    )

    fun getSavedArticles() = dao.getSavedArticles()

    suspend fun insert(article: Article) = dao.insert(article)

    suspend fun delete(article: Article) = dao.delete(article)

}