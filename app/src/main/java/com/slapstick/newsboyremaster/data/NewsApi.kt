package com.slapstick.newsboyremaster.data

import com.slapstick.newsboyremaster.data.models.NewsResponse
import com.slapstick.newsboyremaster.util.Constants.Companion.API_KEY
import com.slapstick.newsboyremaster.util.Constants.Companion.DEFAULT_COUNTRY_CODE
import com.slapstick.newsboyremaster.util.Constants.Companion.DEFAULT_PAGE
import com.slapstick.newsboyremaster.util.Constants.Companion.PAGE_REQUEST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("page") page: Int = DEFAULT_PAGE,
        @Query("pageSize") pageSize: Int = PAGE_REQUEST,
        @Query("country") countryCode: String = DEFAULT_COUNTRY_CODE,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>

    // Never forget to check your @Query values
    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("page") page: Int = DEFAULT_PAGE,
        @Query("pageSize") pageSize: Int = 100,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>

}