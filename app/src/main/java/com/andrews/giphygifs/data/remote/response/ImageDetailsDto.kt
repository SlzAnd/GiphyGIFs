package com.andrews.giphygifs.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageDetailsDto(
    @SerialName("url")
    val url: String,
    @SerialName("width")
    val width: String,
    @SerialName("height")
    val height: String
)