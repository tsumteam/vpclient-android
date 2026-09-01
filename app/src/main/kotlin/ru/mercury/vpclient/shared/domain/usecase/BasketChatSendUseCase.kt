@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.SaveProductMessageRequest
import ru.mercury.vpclient.shared.data.network.response.ProductPayloadResponse
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

class BasketChatSendUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    private val messengerMessagesUseCase: MessengerMessagesUseCase,
    dispatchers: SharedDispatchers
): UseCase<String, Unit>(dispatchers.io) {

    override suspend fun execute(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return

        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = SaveProductMessageRequest(
                    pairedUserId = pairedUserId,
                    localCreateTime = OffsetDateTime.now().format(DATE_TIME_FORMATTER),
                    localMessageId = UUID.randomUUID().toString(),
                    payload = ProductPayloadResponse(text = trimmed)
                )
                networkService.basketChatSend(request)
            },
            onSuccess = { messengerMessagesUseCase(Unit).getOrThrow() },
            onEmpty = { messengerMessagesUseCase(Unit).getOrThrow() },
            onFailure = { error -> throw BasketChatSendException(error.message) }
        )
    }

    data class BasketChatSendException(
        override val message: String
    ): ClientException(message)

    private companion object {
        private val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
    }
}
