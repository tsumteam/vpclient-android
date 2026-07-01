package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationResult
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationResultDeliveryLine
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.GetListClientAddressForCheckoutRequest
import ru.mercury.vpclient.shared.data.network.request.TransferBasketToFittingRequest
import ru.mercury.vpclient.shared.data.network.response.TransferBasketToFittingLineResponse
import ru.mercury.vpclient.shared.data.network.type.DeliveryType
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.data.network.type.KittingType
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.deliveryTimeDto
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.itemsCount
import javax.inject.Inject

// fixme
class ConfirmFittingUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<ConfirmFittingUseCase.Params, FittingConfirmationResult>(dispatchers.io) {

    override suspend fun execute(params: Params): FittingConfirmationResult {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            throw ConfirmFittingException("Не удалось определить клиента")
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
        }.orEmpty()
        val addressComment = when (params.fittingType) {
            FittingType.NONE -> ""
            FittingType.IN_THE_STORE -> ""
            FittingType.AT_HOME -> params.clientAddress?.comment ?: addresses.clientAddress?.comment.orEmpty()
        }
        val lines = when {
            params.useSingleDelivery -> {
                params.products.map { product ->
                    TransferBasketToFittingLineResponse(
                        lineId = product.id,
                        deliveryTime = params.singleInterval?.deliveryTimeDto
                    )
                }
            }
            else -> {
                params.deliveryGroups.flatMap { group ->
                    val interval = group.intervals.firstOrNull { interval ->
                        interval.id == params.selectedDeliveryIntervalIds[group.id]
                    }
                    group.products.map { product ->
                        TransferBasketToFittingLineResponse(
                            lineId = product.id,
                            deliveryTime = interval?.deliveryTimeDto
                        )
                    }
                }
            }
        }
        val result = FittingConfirmationResult(
            deliveryLines = when {
                params.useSingleDelivery -> {
                    listOf(
                        FittingConfirmationResultDeliveryLine(
                            intervalSummary = params.singleInterval?.summary.orEmpty(),
                            productsCount = params.products.itemsCount
                        )
                    )
                }
                else -> {
                    params.deliveryGroups.map { group ->
                        val interval = group.intervals.firstOrNull { interval ->
                            interval.id == params.selectedDeliveryIntervalIds[group.id]
                        }
                        FittingConfirmationResultDeliveryLine(
                            intervalSummary = interval?.summary.orEmpty(),
                            productsCount = group.products.itemsCount
                        )
                    }
                }
            },
            address = address
        )

        handleResponse(
            request = {
                val request = TransferBasketToFittingRequest(
                    pairedUserId = pairedUserId,
                    lines = lines,
                    address = address,
                    addressComment = addressComment,
                    fittingType = params.fittingType,
                    deliveryType = DeliveryType.LOGISTIC,
                    kittingType = KittingType.LOGISTIC
                )
                networkService.fittingsTransferFromBasket(request)
            },
            onSuccess = {},
            onEmpty = {},
            onFailure = { error -> throw ConfirmFittingException(error.message) }
        )

        return result
    }

    data class ConfirmFittingException(
        override val message: String
    ): ClientException(message)

    data class Params(
        val products: List<CartProduct>,
        val fittingType: FittingType,
        val clientAddress: ClientDeliveryAddressEntity?,
        val singleInterval: FittingConfirmationDeliveryInterval?,
        val deliveryGroups: List<FittingConfirmationDeliveryGroup>,
        val selectedDeliveryIntervalIds: Map<String, String>,
        val useSingleDelivery: Boolean
    )
}
