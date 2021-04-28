package com.slapstick.newsboyremaster.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.slapstick.newsboyremaster.data.models.Article
import com.slapstick.newsboyremaster.data.models.Source
import com.slapstick.newsboyremaster.util.Constants.Companion.DB_VERSION

@Database(entities = [Article::class], version = DB_VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun dao(): NewsDao
}

class Converters {
    @TypeConverter
    fun convertFromSource(source: Source): String = source.name

    @TypeConverter
    fun convertToSource(sourceName: String): Source = Source(sourceName, sourceName)
}