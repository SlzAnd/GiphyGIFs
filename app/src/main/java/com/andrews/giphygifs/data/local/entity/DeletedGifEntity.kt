package com.andrews.giphygifs.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_gifs")
data class DeletedGifEntity(
    @PrimaryKey
    val id: String
)