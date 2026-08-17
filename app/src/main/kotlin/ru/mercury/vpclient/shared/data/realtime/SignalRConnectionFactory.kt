package ru.mercury.vpclient.shared.data.realtime

import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.TransportEnum
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignalRConnectionFactory @Inject constructor(
    private val retryPolicy: RealtimeRetryPolicy
) {

    fun create(
        session: RealtimeSession,
        hubName: String,
        registerHandlers: (HubConnection) -> Unit,
        onConnected: suspend (HubConnection) -> Unit
    ): SignalRConnection {
        return SignalRConnection(
            hubName = hubName,
            hubConnectionFactory = {
                HubConnectionBuilder.create("${session.baseUrl}$hubName")
                    .withTransport(TransportEnum.WEBSOCKETS)
                    .withHeaders(session.headers)
                    .withAccessTokenProvider(Single.just(session.token))
                    .build()
            },
            retryPolicy = retryPolicy,
            registerHandlers = registerHandlers,
            onConnected = onConnected
        )
    }
}
