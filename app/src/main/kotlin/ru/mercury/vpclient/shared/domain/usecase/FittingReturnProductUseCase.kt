@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.serialization.json.encodeToJsonElement
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.FittingOperationRequest
import ru.mercury.vpclient.shared.data.network.response.FittingReturnProductOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.type.FittingOperationRequestType
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.fittingOperationJson
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

// fixme
class FittingReturnProductUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<CartProduct, Unit>(dispatchers.io) {

    override suspend fun execute(product: CartProduct) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = FittingOperationRequest(
                    pairedUserId = pairedUserId,
                    items = listOf(
                        fittingOperationJson.encodeToJsonElement(
                            FittingReturnProductOperationRequestItemResponse(
                                operationType = FittingOperationRequestType.RETURN_PRODUCT,
                                operationOrder = 0,
                                lineId = product.id
                            )
                        )
                    )
                )
                networkService.fittingsAddOperations(request)
            },
            onSuccess = {},
            onFailure = { error -> throw FittingReturnProductException(error.message) }
        )
    }

    data class FittingReturnProductException(
        override val message: String
    ): ClientException(message)
}
