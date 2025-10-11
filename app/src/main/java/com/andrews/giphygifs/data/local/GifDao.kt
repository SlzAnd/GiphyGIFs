package com.andrews.giphygifs.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andrews.giphygifs.data.local.entity.GifEntity

@Dao
interface GifDao {

    @Query("SELECT * FROM gifs WHERE id NOT IN (SELECT id FROM deleted_gifs)")
    fun getAllGifs(): PagingSource<Int, GifEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGifs(gifs: List<GifEntity>)

    @Query("DELETE FROM gifs WHERE id = :id")
    suspend fun deleteGif(id: String)

    @Query("DELETE FROM gifs")
    suspend fun clearAll()
}