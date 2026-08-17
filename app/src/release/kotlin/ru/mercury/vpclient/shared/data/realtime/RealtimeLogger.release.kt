package ru.mercury.vpclient.shared.data.realtime

@Suppress("UNUSED_PARAMETER")
fun logRealtimeEvent(
    hubName: String,
    direction: String,
    methodName: String,
    payload: Any? = null
) {
    return
}

@Suppress("UNUSED_PARAMETER")
fun logRealtimeState(
    hubName: String,
    state: String,
    retryAttempt: Int,
    throwable: Throwable? = null
) {
    return
}
