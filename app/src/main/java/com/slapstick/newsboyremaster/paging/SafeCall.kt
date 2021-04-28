package com.slapstick.newsboyremaster.paging

import android.util.Log
import com.slapstick.newsboyremaster.data.models.NewsResponse
import com.slapstick.newsboyremaster.util.ApiEvent
import retrofit2.Response

inline fun safeCall(apiCall: () -> Response<NewsResponse>): ApiEvent =
    try {
        val result = apiCall.invoke()
        if(result.isSuccessful) {
            Log.d("apicall", "Success!")
            val resultBody = result.body()
            if (resultBody != null && resultBody.articles.isNotEmpty()) {
                ApiEvent.Success(resultBody.articles)
            } else {
                ApiEvent.Empty
            }
        } else {
            Log.d("apicall", "Failure: ${result.code()}")
            ApiEvent.Error("Code: ${result.code()} - Error: ${result.message()} - ${result.errorBody()}")
        }
    } catch (e: Exception) {
        ApiEvent.Error(e.message ?: "An unknown error occurred...")
    }