package ru.mercury.vpclient.core.network

import okhttp3.Interceptor

fun provideLoggingInterceptor(): Interceptor {
    return Interceptor { chain -> chain.proceed(chain.request()) }
}
