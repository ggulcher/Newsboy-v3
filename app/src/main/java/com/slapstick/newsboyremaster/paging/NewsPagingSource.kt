package com.slapstick.newsboyremaster.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.slapstick.newsboyremaster.data.models.Article
import com.slapstick.newsboyremaster.util.ApiEvent

class NewsPagingSource(
    private val paging: suspend (position: Int, loadSize: Int) -> ApiEvent
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {

        val position = params.key ?: STARTING_PAGE_INDEX

        return when (val apiEvent = paging.invoke(position, params.loadSize)) {
            is ApiEvent.Error -> LoadResult.Error(Throwable(apiEvent.message))
            is ApiEvent.Success -> LoadResult.Page(
                data = apiEvent.data,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (apiEvent.data.isEmpty()) null else position + 1
            )
            else -> LoadResult.Error(Throwable("No Data Found."))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }
}

private const val STARTING_PAGE_INDEX = 1