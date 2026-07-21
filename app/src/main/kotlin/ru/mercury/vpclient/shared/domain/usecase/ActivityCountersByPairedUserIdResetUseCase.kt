@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.ResetActivityCountersRequest
import ru.mercury.vpclient.shared.data.network.type.ActivityCounterType
import ru.mercury.vpclient.shared.data.persistence.database.dao.ActivityCounterDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.ActivityCounterEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

class ActivityCountersByPairedUserIdResetUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    private val activityCounterDao: ActivityCounterDao,
    dispatchers: SharedDispatchers
): UseCase<ActivityCounterType, Unit>(dispatchers.io) {

    override suspend fun execute(type: ActivityCounterType) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = ResetActivityCountersRequest(type = type)
                networkService.activityCountersByPairedUserIdReset(pairedUserId, request)
            },
            onSuccess = {
                activityCounterDao.upsert(listOf(ActivityCounterEntity(type = type.name, value = 0)))
            },
            onFailure = { error -> throw ActivityCountersByPairedUserIdResetException(error.message) }
        )
    }

    data class ActivityCountersByPairedUserIdResetException(
        override val message: String
    ): ClientException(message)
}
