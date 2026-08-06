package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CancellationException
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.OrderCreationRequest
import ru.mercury.vpclient.shared.data.network.type.DeliveryType
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.data.network.type.PaymentType
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.deliveryTimeDto
import ru.mercury.vpclient.shared.domain.usecase.OrdersCreateFromBasketUseCase.Params
import javax.inject.Inject

// fixme
class OrdersCreateFromBasketUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Params, String>(dispatchers.io) {

    override suspend fun execute(params: Params): String {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser)
            ?.takeIf(String::isNotBlank)
            ?: throw OrdersCreateFromBasketException("Не удалось определить пользователя")
        val address = when (params.fittingType) {
            FittingType.AT_HOME -> params.clientAddress?.address
            FittingType.NONE, FittingType.IN_THE_STORE -> params.boutiqueAddress
        }
        val addressComment = when (params.fittingType) {
            FittingType.AT_HOME -> params.clientAddress?.comment.orEmpty()
            FittingType.NONE, FittingType.IN_THE_STORE -> ""
        }

        val response = try {
            networkService.ordersCreateFromBasket(
                OrderCreationRequest(
                    pairedUserId = pairedUserId,
                    paymentType = params.paymentType,
                    deliveryTime = params.interval?.deliveryTimeDto,
                    fittingType = params.fittingType,
                    deliveryType = DeliveryType.LOGISTIC,
                    latitude = params.clientAddress?.latitude,
                    longitude = params.clientAddress?.longitude,
                    address = address,
                    addressComment = addressComment
                )
            )
        } catch (throwable: CancellationException) {
            throw throwable
        } catch (throwable: Throwable) {
            throw OrdersCreateFromBasketException(throwable.message.orEmpty())
        }

        val error = response.error
        return when {
            error != null && error.code == PRICE_CHANGED_ERROR_CODE -> {
                throw OrderPriceChangedException((error.display ?: error.msg).orEmpty())
            }
            error != null -> throw OrdersCreateFromBasketException((error.display ?: error.msg).orEmpty())
            else -> {
                response.data?.orderNumber
                    ?.takeIf(String::isNotBlank)
                    ?: throw OrdersCreateFromBasketException("Не удалось определить заказ")
            }
        }
    }

    data class Params(
        val paymentType: PaymentType,
        val fittingType: FittingType,
        val interval: FittingConfirmationDeliveryInterval?,
        val clientAddress: ClientDeliveryAddressEntity?,
        val boutiqueAddress: String?
    )

    data class OrdersCreateFromBasketException(
        override val message: String
    ): ClientException(message)

    data class OrderPriceChangedException(
        override val message: String
    ): ClientException(message)

    private companion object {
        private const val PRICE_CHANGED_ERROR_CODE = 102
    }
}
