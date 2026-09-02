package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.MessageGetRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.MessengerMessageDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.entities
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

class BasketChatGetUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val messengerMessageDao: MessengerMessageDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        val response = handleResponseResult(
            request = {
                val request = MessageGetRequest(
                    pairedUserId = pairedUserId,
                    fromMessageId = messengerMessageDao.maxMessageId(),
                    limit = NEW_MESSAGES_LIMIT,
                    toBackward = false
                )
                networkService.basketChatGet(request)
            }
        ).getOrThrow()
        val entities = response.items.orEmpty().entities
        if (entities.isEmpty()) return

        messengerMessageDao.upsert(entities)
    }

    companion object {
        const val POLL_INTERVAL_MILLIS = 1_000L

        private const val NEW_MESSAGES_LIMIT = 10
    }
}
