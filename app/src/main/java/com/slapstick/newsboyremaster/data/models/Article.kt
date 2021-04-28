package com.slapstick.newsboyremaster.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Article(
    @PrimaryKey val url: String,
    val author: String? = "",
    val content: String? = "",
    val description: String? = "",
    val publishedAt: String? = "",
    val source: Source? = Source("", ""),
    val title: String? = "",
    val urlToImage: String? = "",
    val isSaved: Boolean = false
) : Parcelable