package com.slapstick.newsboyremaster.data

import androidx.room.*
import com.slapstick.newsboyremaster.data.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Delete
    suspend fun delete(article: Article)

    @Query("SELECT * FROM article")
    fun getSavedArticles(): Flow<List<Article>>

}