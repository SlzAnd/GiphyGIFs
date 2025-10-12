package com.andrews.giphygifs.domain.model

data class Gif(
    val id: String,
    val title: String,
    val previewUrl: String,
    val fullUrl: String,
    val width: Int,
    val height: Int
)