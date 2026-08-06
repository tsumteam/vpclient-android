package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CancellationException
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.FittingCheckoutData
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.BasketForCheckoutRequest
import ru.mercury.vpclient.shared.data.network.type.CheckoutBonusType
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

class BasketByPairedUserIdForCheckoutUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<CheckoutBonusType, FittingCheckoutData>(dispatchers.io) {

    override suspend fun execute(params: CheckoutBonusType): FittingCheckoutData {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser)
            ?.takeIf(String::isNotBlank)
            ?: throw BasketByPairedUserIdForCheckoutException("Не удалось определить пользователя")
        val response = handleResponseResult(
            request = {
                val request = BasketForCheckoutRequest(
                    bonusType = params
                )
                networkService.basketByPairedUserIdForCheckout(
                    pairedUserId = pairedUserId,
                    request = request
                )
            }
        ).getOrElse { throwable ->
            when (throwable) {
                is CancellationException -> throw throwable
                else -> throw BasketByPairedUserIdForCheckoutException(throwable.message.orEmpty())
            }
        }
        return response.entity
    }

    data class BasketByPairedUserIdForCheckoutException(
        override val message: String
    ): ClientException(message)
}
