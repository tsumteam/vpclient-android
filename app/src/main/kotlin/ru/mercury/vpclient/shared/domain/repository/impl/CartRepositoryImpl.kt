package ru.mercury.vpclient.shared.domain.repository.impl

import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.entity.ActivityCounterTypeRequestEnum
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.itemsCount
import ru.mercury.vpclient.shared.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore
): CartRepository {

    override suspend fun cartItemsCount(): Int {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            return 0
        }

        val cart = handleResponseResult {
            networkService.basketCountByPairedUserId(pairedUserId)
        }.getOrThrow()

        return cart.lines.orEmpty().sumOf { it.itemsCount }
    }

    override suspend fun cartBadge(): Int {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            return 0
        }

        val counters = handleResponseResult {
            networkService.activityCountersByPairedUserId(pairedUserId)
        }.getOrThrow()

        return counters.items.orEmpty()
            .firstOrNull { it.type == ActivityCounterTypeRequestEnum.BASKET }
            ?.value ?: 0
    }
}
