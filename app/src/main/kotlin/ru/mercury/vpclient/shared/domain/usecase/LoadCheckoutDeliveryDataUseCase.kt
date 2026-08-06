package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationData
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.BasketGetDeliveryTimesForOrderRequest
import ru.mercury.vpclient.shared.data.network.request.GetListClientAddressForCheckoutRequest
import ru.mercury.vpclient.shared.data.network.type.DeliveryType
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.data.network.type.KittingType
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.fittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

// fixme
class LoadCheckoutDeliveryDataUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<LoadCheckoutDeliveryDataUseCase.Params, FittingConfirmationData>(dispatchers.io) {

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
            FittingType.NONE, FittingType.IN_THE_STORE -> addresses.boutiqueAddress?.address
            FittingType.AT_HOME -> params.clientAddress?.address ?: addresses.clientAddress?.address
        }
        val addressComment = when (params.fittingType) {
            FittingType.NONE, FittingType.IN_THE_STORE -> ""
            FittingType.AT_HOME -> params.clientAddress?.comment ?: addresses.clientAddress?.comment.orEmpty()
        }

        val deliveryTimes = handleResponseResult(
            request = {
                val request = BasketGetDeliveryTimesForOrderRequest(
                    pairedUserId = pairedUserId,
                    fittingType = params.fittingType,
                    deliveryType = DeliveryType.LOGISTIC,
                    kittingType = KittingType.LOGISTIC,
                    address = address,
                    addressComment = addressComment
                )
                networkService.ordersDeliveryTimes(request)
            }
        ).getOrThrow()

        return FittingConfirmationData(
            boutiqueAddress = addresses.boutiqueAddress?.address,
            clientAddress = addresses.clientAddress?.address,
            isClientAddressAvailable = addresses.controls?.isDeliveryToClientAvailable ?: true,
            singleIntervals = deliveryTimes.deliveryTimes.orEmpty().map { it.fittingConfirmationDeliveryInterval }
        )
    }

    data class Params(
        val fittingType: FittingType,
        val clientAddress: ClientDeliveryAddressEntity?
    )
}
