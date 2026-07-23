package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.DigineticaIdentity
import ru.mercury.vpclient.shared.data.network.DigineticaNetworkService
import ru.mercury.vpclient.shared.data.network.request.DigineticaSearchEventRequest
import ru.mercury.vpclient.shared.domain.usecase.DigineticaSearchEventUseCase.Params
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DigineticaSearchEventUseCase @Inject constructor(
    private val networkService: DigineticaNetworkService,
    dispatchers: SharedDispatchers
): UseCase<Params, Unit>(dispatchers.io) {

    private val trackingScope = CoroutineScope(SupervisorJob() + dispatchers.io)

    override suspend fun execute(params: Params) {
        trackingScope.launch {
            runCatching {
                val request = DigineticaSearchEventRequest(
                    apiKey = DIGINETICA_API_KEY,
                    channel = "MOBILE_APP",
                    sessionId = DigineticaIdentity.sessionId,
                    userGuid = DigineticaIdentity.userGuid,
                    viewGuid = UUID.randomUUID().toString(),
                    eventType = "SEARCH_EVENT",
                    pageNumber = params.pageNumber.toString(),
                    pageProducts = params.pageProducts,
                    searchTerm = params.searchTerm
                )
                networkService.searchEvent(request)
            }
        }
    }

    data class Params(
        val pageNumber: Int,
        val pageProducts: List<String>,
        val searchTerm: String
    )

    private companion object {
        private const val DIGINETICA_API_KEY = "NA861M158O"
    }
}
