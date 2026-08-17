package ru.mercury.vpclient.shared.data.realtime

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRealtimeDataSource @Inject constructor(
    private val connectionFactory: SignalRConnectionFactory
) {

    private val connectionMutex = Mutex()
    private val mutableAllUpdates = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val allUpdates: Flow<Unit> = mutableAllUpdates.asSharedFlow()

    private val mutableEmployeeUpdates = Channel<String>(capacity = Channel.UNLIMITED)
    val employeeUpdates: Flow<String> = mutableEmployeeUpdates.receiveAsFlow()

    suspend fun connect(session: RealtimeSession) {
        connectionMutex.withLock {
            connectionFactory.create(
                session = session,
                hubName = HUB_NAME,
                registerHandlers = { hubConnection ->
                    hubConnection.on(BROADCAST_ALL_UPDATES) {
                        logRealtimeEvent(HUB_NAME, DIRECTION_IN, BROADCAST_ALL_UPDATES)
                        mutableAllUpdates.tryEmit(Unit)
                    }
                    hubConnection.on(
                        BROADCAST_EMPLOYEE_UPDATES, { employeeId ->
                            logRealtimeEvent(HUB_NAME, DIRECTION_IN, BROADCAST_EMPLOYEE_UPDATES, employeeId)
                            mutableEmployeeUpdates.trySend(employeeId)
                        },
                        String::class.java
                    )
                },
                onConnected = { mutableAllUpdates.emit(Unit) }
            ).run()
        }
    }

    private companion object {
        private const val HUB_NAME = "client"
        private const val DIRECTION_IN = "IN"
        private const val BROADCAST_ALL_UPDATES = "BroadcastMyEmployeeScreenUpdatesForAll"
        private const val BROADCAST_EMPLOYEE_UPDATES = "BroadcastMyEmployeeScreenUpdatesForEmployee"
    }
}
