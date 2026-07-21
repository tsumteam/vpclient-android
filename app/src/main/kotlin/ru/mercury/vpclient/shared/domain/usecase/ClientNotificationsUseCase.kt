@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import javax.inject.Inject
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.ClientNotificationCategory
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.ClientNotificationsRequest
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.ClientNotificationDao
import ru.mercury.vpclient.shared.domain.mapper.entities
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.requestJson

class ClientNotificationsUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val database: AppDatabase,
    private val clientNotificationDao: ClientNotificationDao,
    dispatchers: SharedDispatchers
): UseCase<ClientNotificationCategory, Unit>(dispatchers.io) {

    override suspend fun execute(category: ClientNotificationCategory) {
        handleResponse(
            request = {
                val request = ClientNotificationsRequest()
                networkService.clientNotificationsFilters(request)
            },
            onSuccess = { filtersResponse ->
                val filters = when (category) {
                    ClientNotificationCategory.ALL -> emptyList()
                    else -> {
                        val filter = filtersResponse.filters.orEmpty()
                            .sortedBy { response -> response.order ?: Int.MAX_VALUE }
                            .getOrNull(category.ordinal)
                            ?: throw ClientNotificationsException("Не удалось загрузить фильтр уведомлений")
                        listOfNotNull(filter.requestJson())
                    }
                }
                handleResponse(
                    request = {
                        val request = ClientNotificationsRequest(filters = filters)
                        networkService.clientNotifications(
                            limit = CLIENT_NOTIFICATIONS_LIMIT,
                            request = request
                        )
                    },
                    onSuccess = { response ->
                        val entities = response.items.orEmpty().entities(category)
                        database.withTransaction {
                            clientNotificationDao.delete(category)
                            clientNotificationDao.upsert(entities)
                        }
                    },
                    onFailure = { error -> throw ClientNotificationsException(error.message) }
                )
            },
            onFailure = { error -> throw ClientNotificationsException(error.message) }
        )
    }

    data class ClientNotificationsException(
        override val message: String
    ): ClientException(message)

    private companion object {
        private const val CLIENT_NOTIFICATIONS_LIMIT = 15
    }
}
