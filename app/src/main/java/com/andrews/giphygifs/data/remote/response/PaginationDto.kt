package com.andrews.giphygifs.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationDto(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("count")
    val count: Int,
    @SerialName("offset")
    val offset: Int
)