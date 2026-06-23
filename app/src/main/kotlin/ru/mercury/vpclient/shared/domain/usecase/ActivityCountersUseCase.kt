package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.response.AggregatedActivityCounterItemResponse
import ru.mercury.vpclient.shared.data.persistence.database.dao.ActivityCounterDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.ActivityCounterEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.orEmpty
import javax.inject.Inject

class ActivityCountersUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    private val activityCounterDao: ActivityCounterDao,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                networkService.activityCountersByPairedUserId(pairedUserId)
            },
            onSuccess = { response ->
                val entities = response.items.orEmpty().mapNotNull { item -> item.entity() }
                activityCounterDao.upsert(entities)
            }
        )
    }

    private fun AggregatedActivityCounterItemResponse.entity(): ActivityCounterEntity? {
        val type = type?.name ?: return null
        return ActivityCounterEntity(
            type = type,
            value = value.orEmpty
        )
    }
}
