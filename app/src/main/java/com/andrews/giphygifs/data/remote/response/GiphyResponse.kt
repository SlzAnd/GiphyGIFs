package com.andrews.giphygifs.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GiphyResponse(
    @SerialName("data")
    val data: List<GifDto>,
    @SerialName("pagination")
    val pagination: PaginationDto
)
