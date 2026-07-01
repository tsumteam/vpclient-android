package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.type.ActivityCounterType
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

// fixme
class CartBadgeUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, Int>(dispatchers.io) {

    override suspend fun execute(params: Unit): Int {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            return 0
        }

        val counters = handleResponseResult {
            networkService.activityCountersByPairedUserId(pairedUserId)
        }.getOrThrow()

        return counters.items.orEmpty()
            .firstOrNull { it.type == ActivityCounterType.BASKET }
            ?.value ?: 0
    }
}
