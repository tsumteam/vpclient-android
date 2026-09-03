package ru.mercury.vpclient

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import ru.mercury.vpclient.shared.data.network.ImageRequestRetryInterceptor

@HiltAndroidApp
class App: Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun newImageLoader(): ImageLoader {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ImageRequestRetryInterceptor())
            .build()

        return ImageLoader.Builder(this)
            .okHttpClient(okHttpClient)
            .build()
    }
}
