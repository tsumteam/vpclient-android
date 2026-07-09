package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.serialization.json.encodeToJsonElement
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.FittingOperationRequest
import ru.mercury.vpclient.shared.data.network.response.FittingChangeLineColorOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.type.FittingOperationRequestType
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.fittingDeliveryForProduct
import ru.mercury.vpclient.shared.domain.mapper.fittingOperationJson
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

// fixme
class SetFittingProductColorUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<SetFittingProductColorUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val delivery = networkService.fittingDeliveryForProduct(pairedUserId, params.product)
                val request = FittingOperationRequest(
                    pairedUserId = pairedUserId,
                    items = listOf(
                        fittingOperationJson.encodeToJsonElement(
                            FittingChangeLineColorOperationRequestItemResponse(
                                operationType = FittingOperationRequestType.CHANGE_LINE_COLOR,
                                operationOrder = 0,
                                lineId = params.product.id,
                                colorId = params.colorId,
                                deliveryTime = delivery?.deliveryTime,
                                address = delivery?.address,
                                addressComment = delivery?.addressComment,
                                fittingType = delivery?.fittingType,
                                deliveryType = delivery?.deliveryType,
                                kittingType = delivery?.kittingType,
                                leaveItemsToClient = delivery?.leaveItemsToClient,
                                isGoWithCourier = delivery?.isGoWithCourier,
                                packOrderMyself = delivery?.packOrderMyself,
                                manageClientCommunicationsMyself = delivery?.manageClientCommunicationsMyself,
                                deliveryComment = delivery?.deliveryComment
                            )
                        )
                    )
                )
                networkService.fittingsAddOperations(request)
            },
            onSuccess = {},
            onFailure = { error -> throw ChangeFittingLineColorException(error.message) }
        )
    }

    data class Params(
        val product: CartProduct,
        val colorId: String
    )

    data class ChangeFittingLineColorException(
        override val message: String
    ): ClientException(message)
}
