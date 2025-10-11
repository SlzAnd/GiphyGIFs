package com.andrews.giphygifs.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.andrews.giphygifs.BuildConfig
import com.andrews.giphygifs.data.local.GiphyDatabase
import com.andrews.giphygifs.data.local.entity.GifEntity
import com.andrews.giphygifs.data.local.entity.RemoteKeysEntity
import com.andrews.giphygifs.data.mappers.toEntityList
import com.andrews.giphygifs.data.remote.GiphyApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GifRemoteMediator(
    private val query: String,
    private val api: GiphyApi,
    private val database: GiphyDatabase
) : RemoteMediator<Int, GifEntity>() {

    private val apiKey = BuildConfig.GIPHY_API_KEY
    private val gifDao = database.gifDao()
    private val deletedGifDao = database.deletedGifDao()
    private val remoteKeysDao = database.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GifEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> INITIAL_PAGE
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = false)
                    nextPage
                }
            }

            val response = if (query.isEmpty()) {
                api.getTrendingGifs(
                    apiKey = apiKey,
                    limit = state.config.pageSize,
                    offset = page * state.config.pageSize
                )
            } else {
                api.searchGifs(
                    apiKey = apiKey,
                    limit = state.config.pageSize,
                    offset = page * state.config.pageSize,
                    query = query
                )
            }

            val gifs = response.data

            // Filter out deleted GIFs FIRST
            val deletedGifIds = deletedGifDao.getAllDeletedGifIds()
            val filteredGifs = gifs.filterNot { gif -> deletedGifIds.contains(gif.id) }

            val pagination = response.pagination
            val endOfPaginationReached = filteredGifs.isEmpty() ||
                    pagination.offset + pagination.count >= pagination.totalCount

            if (filteredGifs.isEmpty() && gifs.isNotEmpty()) {
                return MediatorResult.Success(endOfPaginationReached = false)
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    gifDao.clearAll()
                }

                val prevKey = if (page == INITIAL_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + (gifs.size / state.config.pageSize)
                val remoteKeys = filteredGifs.map { gif ->
                    RemoteKeysEntity(
                        gifId = gif.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                remoteKeysDao.insertAll(remoteKeys)
                gifDao.insertGifs(filteredGifs.toEntityList())
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, GifEntity>
    ): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { gif ->
            remoteKeysDao.getRemoteKeyByGifId(gif.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, GifEntity>
    ): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { gif ->
            val remoteKey = remoteKeysDao.getRemoteKeyByGifId(gif.id)
            remoteKey
        }
    }

    companion object Companion {
        private const val INITIAL_PAGE = 0
    }
}