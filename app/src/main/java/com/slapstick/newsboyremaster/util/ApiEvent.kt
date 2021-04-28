package com.slapstick.newsboyremaster.util

import com.slapstick.newsboyremaster.data.models.Article

sealed class ApiEvent {
    class Success(val data: List<Article>) : ApiEvent()
    class Error(val message: String) : ApiEvent()
    object Empty : ApiEvent()
    object Loading : ApiEvent()
}