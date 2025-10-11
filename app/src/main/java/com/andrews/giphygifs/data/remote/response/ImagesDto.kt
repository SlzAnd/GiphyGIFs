package com.andrews.giphygifs.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesDto(
    @SerialName("fixed_height")
    val fixedHeight: ImageDetailsDto,
    @SerialName("downsized_medium")
    val downsizedMedium: ImageDetailsDto? = null,
    @SerialName("original")
    val original: ImageDetailsDto? = null
)