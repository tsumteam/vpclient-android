package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CancellationException
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.SaveProductMessageRequest
import ru.mercury.vpclient.shared.data.network.response.ProductPayloadResponse
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.entities
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.usecase.BasketChatSendUseCase.Params
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

class BasketChatSendUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Params, Long?>(dispatchers.io) {

    override suspend fun execute(params: Params): Long? {
        val trimmed = params.text.trim()
        if (trimmed.isEmpty()) return null

        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return null

        val request = SaveProductMessageRequest(
            pairedUserId = pairedUserId,
            localCreateTime = OffsetDateTime.now().format(DATE_TIME_FORMATTER),
            localMessageId = UUID.randomUUID().toString(),
            payload = ProductPayloadResponse(
                text = trimmed,
                citation = params.citation,
                citatedMessageId = params.citatedMessageId?.toInt()
            )
        )
        val response = handleResponseResult {
            networkService.basketChatSend(request)
        }.getOrElse { throwable ->
            if (throwable is CancellationException) throw throwable
            throw BasketChatSendException(throwable.message.orEmpty())
        }
        val entity = listOf(response).entities.singleOrNull() ?: return null

        return entity.id
    }

    data class Params(
        val text: String,
        val citatedMessageId: Long? = null,
        val citation: String? = null
    )

    data class BasketChatSendException(
        override val message: String
    ): ClientException(message)

    private companion object {
        private val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
    }
}
