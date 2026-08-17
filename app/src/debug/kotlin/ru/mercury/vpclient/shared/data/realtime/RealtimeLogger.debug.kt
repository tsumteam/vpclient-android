package ru.mercury.vpclient.shared.data.realtime

import timber.log.Timber

fun logRealtimeEvent(
    hubName: String,
    direction: String,
    methodName: String,
    payload: Any? = null
) {
    Timber.tag(REALTIME_LOG_TAG).d(
        "[%s] %s %s payload=%s",
        hubName,
        direction,
        methodName,
        payload ?: EMPTY_PAYLOAD
    )
    return
}

fun logRealtimeState(
    hubName: String,
    state: String,
    retryAttempt: Int,
    throwable: Throwable? = null
) {
    Timber.tag(REALTIME_LOG_TAG).d(throwable, "[%s] %s retryAttempt=%d", hubName, state, retryAttempt)
    return
}

private const val REALTIME_LOG_TAG = "SignalR"
private const val EMPTY_PAYLOAD = "[]"
