package com.slapstick.newsboyremaster.di

import android.content.Context
import androidx.room.Room
import com.slapstick.newsboyremaster.data.NewsApi
import com.slapstick.newsboyremaster.data.NewsDao
import com.slapstick.newsboyremaster.data.NewsDatabase
import com.slapstick.newsboyremaster.util.Constants.Companion.BASE_URL
import com.slapstick.newsboyremaster.util.Constants.Companion.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    @Singleton
    fun provideNewsAPI(): NewsApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): NewsDatabase =
        Room.databaseBuilder(context, NewsDatabase::class.java, DB_NAME).build()

    @Singleton
    @Provides
    fun provideDAO(db: NewsDatabase): NewsDao = db.dao()

}