package com.andrews.giphygifs.data.mappers

import com.andrews.giphygifs.data.local.entity.GifEntity
import com.andrews.giphygifs.data.remote.response.GifDto
import com.andrews.giphygifs.domain.model.Gif

fun GifDto.toEntity(): GifEntity {
    val fullImgUrl = images.downsizedMedium ?: images.original ?: images.fixedHeight
    return GifEntity(
        id = id,
        title = title,
        previewUrl = images.fixedHeight.url,
        fullUrl = fullImgUrl.url,
        width = fullImgUrl.width.toIntOrNull() ?: 0,
        height = fullImgUrl.height.toIntOrNull() ?: 0
    )
}

fun GifEntity.toDomain(): Gif {
    return Gif(
        id = id,
        title = title,
        previewUrl = previewUrl,
        fullUrl = fullUrl,
        width = width,
        height = height
    )
}

fun List<GifDto>.toEntityList(): List<GifEntity> = map { it.toEntity() }