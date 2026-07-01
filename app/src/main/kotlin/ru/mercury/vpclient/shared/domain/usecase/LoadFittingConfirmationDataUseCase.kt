package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationData
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.BasketGetDeliveryTimesForFittingRequest
import ru.mercury.vpclient.shared.data.network.request.GetDeliveryIntervalsForExistingFittingRequest
import ru.mercury.vpclient.shared.data.network.request.GetListClientAddressForCheckoutRequest
import ru.mercury.vpclient.shared.data.network.type.DeliveryType
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.data.network.type.KittingType
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.fittingConfirmationData
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

// fixme
class LoadFittingConfirmationDataUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<LoadFittingConfirmationDataUseCase.Params, FittingConfirmationData>(dispatchers.io) {

    override suspend fun execute(params: Params): FittingConfirmationData {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            return FittingConfirmationData()
        }

        val addresses = handleResponseResult(
            request = {
                val request = GetListClientAddressForCheckoutRequest(
                    pairedUserId = pairedUserId
                )
                networkService.clientAddressCheckout(request)
            }
        ).getOrThrow()
        val address = when (params.fittingType) {
            FittingType.NONE -> addresses.boutiqueAddress?.address
            FittingType.IN_THE_STORE -> addresses.boutiqueAddress?.address
            FittingType.AT_HOME -> params.clientAddress?.address ?: addresses.clientAddress?.address
        }
        val addressComment = when (params.fittingType) {
            FittingType.NONE -> ""
            FittingType.IN_THE_STORE -> ""
            FittingType.AT_HOME -> params.clientAddress?.comment ?: addresses.clientAddress?.comment.orEmpty()
        }

        return when (val deliveryId = params.deliveryId) {
            null -> {
                val deliveryTimes = handleResponseResult(
                    request = {
                        val request = BasketGetDeliveryTimesForFittingRequest(
                            pairedUserId = pairedUserId,
                            basketLineIds = params.products.map { product -> product.id },
                            fittingType = params.fittingType,
                            kittingType = KittingType.LOGISTIC,
                            deliveryType = DeliveryType.LOGISTIC,
                            address = address,
                            addressComment = addressComment
                        )
                        networkService.fittingsDeliveryTimes(request)
                    }
                ).getOrThrow()

                deliveryTimes.fittingConfirmationData(
                    addresses = addresses,
                    selectedProducts = params.products
                )
            }
            else -> {
                val deliveryTimes = handleResponseResult(
                    request = {
                        val request = GetDeliveryIntervalsForExistingFittingRequest(
                            pairedUserId = pairedUserId,
                            deliveryId = deliveryId,
                            fittingLineIds = params.products.map { product -> product.id },
                            fittingType = params.fittingType,
                            kittingType = KittingType.LOGISTIC,
                            deliveryType = DeliveryType.LOGISTIC,
                            address = address,
                            addressComment = addressComment
                        )
                        networkService.fittingsDeliveryTimesForExisingDelivery(request)
                    }
                ).getOrThrow()

                deliveryTimes.fittingConfirmationData(
                    addresses = addresses,
                    deliveryId = deliveryId,
                    selectedProducts = params.products
                )
            }
        }
    }

    data class Params(
        val products: List<CartProduct>,
        val deliveryId: String?,
        val fittingType: FittingType,
        val clientAddress: ClientDeliveryAddressEntity?
    )
}
