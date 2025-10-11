package com.andrews.giphygifs.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andrews.giphygifs.data.local.entity.DeletedGifEntity

@Dao
interface DeletedGifDao {

    @Query("SELECT id FROM deleted_gifs")
    suspend fun getAllDeletedGifIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeletedGif(deletedGif: DeletedGifEntity)
}