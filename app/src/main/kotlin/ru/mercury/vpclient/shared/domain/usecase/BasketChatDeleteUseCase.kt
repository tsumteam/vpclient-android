@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.DeleteMessageRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.MessengerMessageDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class BasketChatDeleteUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val messengerMessageDao: MessengerMessageDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Long, Unit>(dispatchers.io) {

    override suspend fun execute(messageId: Long) {
        val originalEntity = messengerMessageDao.select(messageId) ?: return
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()

        messengerMessageDao.delete(messageId)

        handleResponse(
            request = {
                val request = DeleteMessageRequest(
                    pairedUserId = pairedUserId,
                    messageIds = listOf(messageId)
                )
                networkService.basketChatDelete(request)
            },
            onSuccess = {},
            onFailure = { error ->
                messengerMessageDao.upsert(listOf(originalEntity))
                throw BasketChatDeleteException(error.message)
            }
        )
    }

    data class BasketChatDeleteException(
        override val message: String
    ): ClientException(message)
}
