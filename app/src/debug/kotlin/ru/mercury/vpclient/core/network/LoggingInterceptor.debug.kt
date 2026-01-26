package ru.mercury.vpclient.core.network

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

fun provideLoggingInterceptor(): Interceptor {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    return logging
}
