package com.andrews.giphygifs.domain.model

data class Gif(
    val id: String,
    val title: String,
    val previewUrl: String,  // fixed_height for grid
    val fullUrl: String,     // downsized_medium for full screen
    val width: Int,
    val height: Int
) {
    val aspectRatio: Float
        get() = if (height > 0) width.toFloat() / height.toFloat() else 1f
}