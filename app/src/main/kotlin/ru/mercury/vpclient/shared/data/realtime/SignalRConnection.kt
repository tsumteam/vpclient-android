package ru.mercury.vpclient.shared.data.realtime

import com.microsoft.signalr.HubConnection
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds

class SignalRConnection(
    private val hubName: String,
    private val hubConnectionFactory: () -> HubConnection,
    private val retryPolicy: RealtimeRetryPolicy,
    private val registerHandlers: (HubConnection) -> Unit,
    private val onConnected: suspend (HubConnection) -> Unit
) {

    private val mutex = Mutex()

    suspend fun run() {
        mutex.withLock {
            var retryAttempt = 0
            while (currentCoroutineContext().isActive) {
                val retryDelayMillis = retryPolicy.delayMillis(retryAttempt)
                if (retryDelayMillis > 0L) delay(retryDelayMillis.milliseconds)
                logRealtimeState(hubName, STATE_CONNECTING, retryAttempt)

                var hubConnection: HubConnection? = null
                try {
                    val closed = CompletableDeferred<Throwable?>()
                    hubConnection = hubConnectionFactory()
                    registerHandlers(hubConnection)
                    hubConnection.onClosed { throwable -> closed.complete(throwable) }
                    hubConnection.start().awaitCompletion()
                    logRealtimeState(hubName, STATE_CONNECTED, retryAttempt)
                    onConnected(hubConnection)
                    retryAttempt = 0
                    val closeCause = closed.await()
                    logRealtimeState(hubName, STATE_CLOSED, retryAttempt, closeCause)
                    if (closeCause != null) Timber.w(closeCause, "SignalR connection closed")
                    retryAttempt += 1
                } catch (exception: CancellationException) {
                    throw exception
                } catch (throwable: Throwable) {
                    logRealtimeState(hubName, STATE_FAILED, retryAttempt, throwable)
                    Timber.w(throwable, "SignalR connection failed")
                    retryAttempt += 1
                } finally {
                    if (hubConnection != null) {
                        withContext(NonCancellable) {
                            runCatching {
                                withTimeout(STOP_TIMEOUT_MILLIS.milliseconds) {
                                    hubConnection.stop().awaitCompletion()
                                }
                            }.onFailure { throwable -> Timber.d(throwable, "SignalR stop failed") }
                            logRealtimeState(hubName, STATE_STOPPED, retryAttempt)
                        }
                    }
                }
            }
        }
    }

    private companion object {
        private const val STOP_TIMEOUT_MILLIS = 5_000L
        private const val STATE_CONNECTING = "CONNECTING"
        private const val STATE_CONNECTED = "CONNECTED"
        private const val STATE_CLOSED = "CLOSED"
        private const val STATE_FAILED = "FAILED"
        private const val STATE_STOPPED = "STOPPED"
    }
}
