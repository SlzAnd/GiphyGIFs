package com.andrews.giphygifs.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andrews.giphygifs.data.local.entity.DeletedGifEntity
import com.andrews.giphygifs.data.local.entity.GifEntity
import com.andrews.giphygifs.data.local.entity.RemoteKeysEntity

@Database(
    entities = [
        GifEntity::class,
        DeletedGifEntity::class,
        RemoteKeysEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GiphyDatabase : RoomDatabase() {

    abstract fun gifDao(): GifDao
    abstract fun deletedGifDao(): DeletedGifDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        const val DATABASE_NAME = "giphy_database"
    }
}