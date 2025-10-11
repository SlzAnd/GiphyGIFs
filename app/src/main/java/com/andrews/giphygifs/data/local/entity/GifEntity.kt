package com.andrews.giphygifs.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifs")
data class GifEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val previewUrl: String,
    val fullUrl: String,
    val width: Int,
    val height: Int
)