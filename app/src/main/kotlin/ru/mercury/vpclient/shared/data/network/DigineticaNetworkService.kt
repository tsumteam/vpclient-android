package ru.mercury.vpclient.shared.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import ru.mercury.vpclient.shared.data.network.di.DigineticaHttpClient
import ru.mercury.vpclient.shared.data.network.request.DigineticaSearchEventRequest
import javax.inject.Inject

class DigineticaNetworkService @Inject constructor(
    @DigineticaHttpClient private val ktorHttpClient: HttpClient
) {

    suspend fun searchEvent(request: DigineticaSearchEventRequest) {
        ktorHttpClient.post("event") {
            setBody(request)
        }.bodyAsText()
    }
}
