package ru.mercury.vpclient.shared.data.realtime

import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun Completable.awaitCompletion() {
    return suspendCancellableCoroutine { continuation ->
        val disposable = subscribe(
            { continuation.resume(Unit) },
            { throwable -> continuation.resumeWithException(throwable) }
        )
        continuation.invokeOnCancellation { disposable.dispose() }
    }
}
