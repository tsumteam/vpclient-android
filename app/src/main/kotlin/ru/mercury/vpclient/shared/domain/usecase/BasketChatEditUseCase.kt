package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.EditMessageRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.MessengerMessageDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.usecase.BasketChatEditUseCase.Params
import javax.inject.Inject

class BasketChatEditUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val messengerMessageDao: MessengerMessageDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val trimmed = params.text.trim()
        if (trimmed.isEmpty()) return

        val originalEntity = messengerMessageDao.select(params.messageId) ?: return
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()

        messengerMessageDao.upsert(
            listOf(
                originalEntity.copy(
                    text = trimmed,
                    isEdited = true
                )
            )
        )

        handleResponse(
            request = {
                val request = EditMessageRequest(
                    pairedUserId = pairedUserId,
                    messageId = params.messageId,
                    text = trimmed
                )
                networkService.basketChatEdit(request)
            },
            onSuccess = {},
            onFailure = { error ->
                messengerMessageDao.upsert(listOf(originalEntity))
                throw BasketChatEditException(error.message)
            }
        )
    }

    data class Params(
        val messageId: Long,
        val text: String
    )

    data class BasketChatEditException(
        override val message: String
    ): ClientException(message)
}
