package com.andrews.giphygifs

import android.app.Application
import com.andrews.giphygifs.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GiphyGifsApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GiphyGifsApplication)
            modules(appModule)
        }
    }
}