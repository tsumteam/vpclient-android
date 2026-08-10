package ru.mercury.vpclient.shared.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.mercury.vpclient.shared.data.network.di.SbpBanksHttpClient
import ru.mercury.vpclient.shared.data.network.response.SbpBanksListResponse
import javax.inject.Inject

class SbpBanksNetworkService @Inject constructor(
    @SbpBanksHttpClient private val ktorHttpClient: HttpClient
) {

    suspend fun banks(): SbpBanksListResponse {
        return ktorHttpClient.get("proxyapp/c2bmembers.json").body()
    }
}
