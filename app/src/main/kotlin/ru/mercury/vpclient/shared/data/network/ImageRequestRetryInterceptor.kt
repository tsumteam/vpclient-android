package ru.mercury.vpclient.shared.data.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ImageRequestRetryInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var attempt = 0
        while (true) {
            try {
                return chain.proceed(request)
            } catch (exception: IOException) {
                if (attempt >= MAX_RETRY_ATTEMPTS) throw exception
                val retryDelayMillis = when (attempt) {
                    0 -> FIRST_RETRY_DELAY_MILLIS
                    else -> SECOND_RETRY_DELAY_MILLIS
                }
                Thread.sleep(retryDelayMillis)
                attempt++
            }
        }
    }

    private companion object {
        private const val MAX_RETRY_ATTEMPTS = 2
        private const val FIRST_RETRY_DELAY_MILLIS = 500L
        private const val SECOND_RETRY_DELAY_MILLIS = 1_500L
    }
}
