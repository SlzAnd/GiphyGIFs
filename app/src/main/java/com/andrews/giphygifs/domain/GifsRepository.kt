package com.andrews.giphygifs.domain

import androidx.paging.PagingData
import com.andrews.giphygifs.domain.model.Gif
import kotlinx.coroutines.flow.Flow

interface GifsRepository {

    /**
     * Get paginated GIFs based on search query
     */
    fun getGifs(query: String): Flow<PagingData<Gif>>

    /**
     * Get trending GIFs (empty query means trending + initial call)
     */
    fun getTrendingGifs(): Flow<PagingData<Gif>>

    /**
     * Get only cached gifs, for details screen
     */
    fun getCachedGifsPaged(): Flow<PagingData<Gif>>

    /**
     * Mark a GIF as deleted locally and remove from the local db
     * This GIF will be filtered out from all future queries
     */
    suspend fun deleteGif(gifId: String)
}