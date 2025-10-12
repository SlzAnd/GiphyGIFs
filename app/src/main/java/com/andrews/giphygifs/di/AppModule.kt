package com.andrews.giphygifs.di

import androidx.room.Room
import com.andrews.giphygifs.data.MainRepositoryImpl
import com.andrews.giphygifs.data.local.DeletedGifDao
import com.andrews.giphygifs.data.local.GifDao
import com.andrews.giphygifs.data.local.GiphyDatabase
import com.andrews.giphygifs.data.local.RemoteKeysDao
import com.andrews.giphygifs.data.remote.GiphyApi
import com.andrews.giphygifs.domain.MainRepository
import com.andrews.giphygifs.ui.screen.details.DetailsViewModel
import com.andrews.giphygifs.ui.screen.home.HomeViewModel
import com.andrews.giphygifs.utils.AppConstants.GIPHY_API_BASE_URL
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val appModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single<GiphyDatabase> {
        Room.databaseBuilder(
            this.androidContext(),
            GiphyDatabase::class.java,
            GiphyDatabase.DATABASE_NAME
        )
            .build()
    }

    single<GifDao> {
        get<GiphyDatabase>().gifDao()
    }

    single<DeletedGifDao> {
        get<GiphyDatabase>().deletedGifDao()
    }

    single<RemoteKeysDao> {
        get<GiphyDatabase>().remoteKeysDao()
    }

    single<GiphyApi> {
        Retrofit.Builder()
            .baseUrl(GIPHY_API_BASE_URL)
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(GiphyApi::class.java)
    }

    single<MainRepository> {
        MainRepositoryImpl(api = get(), database = get())
    }

    viewModel {
        HomeViewModel(repository = get())
    }

    viewModel {
        DetailsViewModel(repository = get())
    }
}