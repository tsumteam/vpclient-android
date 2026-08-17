package ru.mercury.vpclient.shared.data.realtime

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRealtimeDataSource @Inject constructor(
    private val connectionFactory: SignalRConnectionFactory
) {

    private val connectionMutex = Mutex()
    private val mutableUpdates = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val updates: Flow<Unit> = mutableUpdates.asSharedFlow()

    suspend fun connect(session: RealtimeSession) {
        connectionMutex.withLock {
            connectionFactory.create(
                session = session,
                hubName = HUB_NAME,
                registerHandlers = { hubConnection ->
                    hubConnection.on(
                        BROADCAST_UPDATES, { payload ->
                            logRealtimeEvent(HUB_NAME, DIRECTION_IN, BROADCAST_UPDATES, payload)
                            mutableUpdates.tryEmit(Unit)
                        },
                        Any::class.java
                    )
                },
                onConnected = { hubConnection ->
                    logRealtimeEvent(HUB_NAME, DIRECTION_OUT, SELECT_ACTIVE_EMPLOYEE, session.pairedUserId)
                    hubConnection.invoke(SELECT_ACTIVE_EMPLOYEE, session.pairedUserId).awaitCompletion()
                    mutableUpdates.emit(Unit)
                }
            ).run()
        }
    }

    private companion object {
        private const val HUB_NAME = "activity"
        private const val DIRECTION_IN = "IN"
        private const val DIRECTION_OUT = "OUT"
        private const val BROADCAST_UPDATES = "BroadcastActivityCounterUpdates"
        private const val SELECT_ACTIVE_EMPLOYEE = "SelectActiveEmployee"
    }
}
