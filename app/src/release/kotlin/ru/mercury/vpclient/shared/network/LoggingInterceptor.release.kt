package ru.mercury.vpclient.shared.network

import okhttp3.Interceptor

fun provideLoggingInterceptor(): Interceptor {
    return Interceptor { chain -> chain.proceed(chain.request()) }
}
