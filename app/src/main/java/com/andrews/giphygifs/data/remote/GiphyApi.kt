package com.andrews.giphygifs.data.remote

import com.andrews.giphygifs.data.remote.response.GiphyResponse
import com.andrews.giphygifs.utils.AppConstants.SEARCH_PATH
import com.andrews.giphygifs.utils.AppConstants.TRENDING_PATH
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {

    @GET(TRENDING_PATH)
    suspend fun getTrendingGifs(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g",
        @Query("bundle") bundle: String = "messaging_non_clips"
    ): GiphyResponse

    @GET(SEARCH_PATH)
    suspend fun searchGifs(
        @Query("api_key") apiKey: String,
        @Query("q") query: String,
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g",
        @Query("lang") lang: String = "en",
        @Query("bundle") bundle: String = "messaging_non_clips"
    ): GiphyResponse
}