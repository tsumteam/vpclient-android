package ru.mercury.vpclient.shared.data.realtime

import javax.inject.Inject

class RealtimeRetryPolicy @Inject constructor() {

    fun delayMillis(attempt: Int): Long {
        return RETRY_DELAYS_MILLIS[attempt.coerceIn(RETRY_DELAYS_MILLIS.indices)]
    }

    private companion object {
        private val RETRY_DELAYS_MILLIS = longArrayOf(0L, 2_000L, 5_000L, 10_000L, 30_000L)
    }
}
