package com.andrews.giphygifs.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.andrews.giphygifs.data.local.DeletedGifDao
import com.andrews.giphygifs.data.local.GifDao
import com.andrews.giphygifs.data.local.GiphyDatabase
import com.andrews.giphygifs.data.local.RemoteKeysDao
import com.andrews.giphygifs.data.local.entity.DeletedGifEntity
import com.andrews.giphygifs.data.mappers.toDomain
import com.andrews.giphygifs.data.paging.GifRemoteMediator
import com.andrews.giphygifs.data.remote.GiphyApi
import com.andrews.giphygifs.domain.GifsRepository
import com.andrews.giphygifs.domain.model.Gif
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GifsRepositoryImpl(
    private val api: GiphyApi,
    private val database: GiphyDatabase,
    private val gifDao: GifDao,
    private val deletedGifDao: DeletedGifDao,
    private val remoteKeysDao: RemoteKeysDao
) : GifsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getGifs(query: String): Flow<PagingData<Gif>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                initialLoadSize = INITIAL_LOAD_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = GifRemoteMediator(
                query = query,
                api = api,
                database = database,
                gifDao = gifDao,
                deletedGifDao = deletedGifDao,
                remoteKeysDao = remoteKeysDao
            ),
            pagingSourceFactory = { gifDao.getAllGifs() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getTrendingGifs(): Flow<PagingData<Gif>> {
        return getGifs("")
    }

    override fun getCachedGifsPaged(): Flow<PagingData<Gif>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 10,
                initialLoadSize = 40,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { gifDao.getAllGifs() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun deleteGif(gifId: String) {
        deletedGifDao.insertDeletedGif(
            DeletedGifEntity(id = gifId)
        )
        gifDao.deleteGif(gifId)
    }

    companion object {
        private const val PAGE_SIZE = 25
        private const val PREFETCH_DISTANCE = 5
        private const val INITIAL_LOAD_SIZE = 25
    }
}