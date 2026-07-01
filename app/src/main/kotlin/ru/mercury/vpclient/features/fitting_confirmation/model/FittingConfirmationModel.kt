package ru.mercury.vpclient.features.fitting_confirmation.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.features.fitting_address_sheet.model.FittingAddressModel
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryMode
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationPlaceType
import ru.mercury.vpclient.shared.data.network.type.FittingType
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity
import ru.mercury.vpclient.shared.domain.mapper.title
import ru.mercury.vpclient.shared.mvi.Model

data class FittingConfirmationModel(
    val productIds: List<String> = emptyList(),
    val deliveryId: String? = null,
    val fittingType: FittingType? = null,
    val products: List<CartProduct> = emptyList(),
    val boutiqueAddress: String? = null,
    val clientAddress: String? = null,
    val clientAddresses: List<ClientDeliveryAddressEntity> = emptyList(),
    val selectedClientAddressId: Int? = null,
    val pendingClientAddressId: Int? = null,
    val addressActionAddressId: Int? = null,
    val deleteAddressId: Int? = null,
    val isAddressFormVisible: Boolean = false,
    val isAddressSearchVisible: Boolean = false,
    val addressForm: FittingAddressModel = FittingAddressModel(),
    val addressListJob: Job? = null,
    val isAddressSaving: Boolean = false,
    val isClientAddressAvailable: Boolean = true,
    val selectedPlaceType: FittingConfirmationPlaceType = FittingConfirmationPlaceType.Boutique,
    val deliveryMode: FittingConfirmationDeliveryMode = FittingConfirmationDeliveryMode.Multiple,
    val singleIntervals: List<FittingConfirmationDeliveryInterval> = emptyList(),
    val deliveryGroups: List<FittingConfirmationDeliveryGroup> = emptyList(),
    val selectedSingleDayId: String? = null,
    val selectedSingleIntervalId: String? = null,
    val selectedDeliveryDayIds: Map<String, String> = emptyMap(),
    val selectedDeliveryIntervalIds: Map<String, String> = emptyMap(),
    val expandedDeliveryId: String? = null,
    val isInitialIntervalsLoading: Boolean = true,
    val isIntervalsLoading: Boolean = false,
    val isConfirmLoading: Boolean = false,
    val intervalsError: String? = null
): Model {
    val isAddressListLoading: Boolean
        get() = addressListJob?.isActive == true

    val selectedProducts: List<CartProduct>
        get() = productIds.mapNotNull { productId ->
            products.firstOrNull { product -> product.id == productId }
        }

    val selectedClientAddress: ClientDeliveryAddressEntity?
        get() = clientAddresses.firstOrNull { address -> address.id == selectedClientAddressId }

    val pendingClientAddress: ClientDeliveryAddressEntity?
        get() = clientAddresses.firstOrNull { address -> address.id == pendingClientAddressId }

    val addressActionAddress: ClientDeliveryAddressEntity?
        get() = clientAddresses.firstOrNull { address -> address.id == addressActionAddressId }

    val deleteAddress: ClientDeliveryAddressEntity?
        get() = clientAddresses.firstOrNull { address -> address.id == deleteAddressId }

    val isAddressActionsSheetVisible: Boolean
        get() = addressActionAddress != null

    val isAddressDeleteDialogVisible: Boolean
        get() = deleteAddress != null

    val displayedClientAddress: String?
        get() = selectedClientAddress?.title ?: clientAddress

    val isSelectedPlaceWithoutAddress: Boolean
        get() = when (selectedPlaceType) {
            FittingConfirmationPlaceType.Boutique -> false
            FittingConfirmationPlaceType.Home -> displayedClientAddress.isNullOrBlank()
            FittingConfirmationPlaceType.Other -> true
        }

    val isMultipleDeliveryAvailable: Boolean
        get() = deliveryGroups.size > 1

    val selectedSingleDayIntervals: List<FittingConfirmationDeliveryInterval>
        get() {
            val selectedDayId = selectedSingleDayId ?: singleIntervals.firstOrNull()?.dayId
            return singleIntervals.filter { interval -> interval.dayId == selectedDayId }
        }

    val selectedSingleInterval: FittingConfirmationDeliveryInterval?
        get() = singleIntervals.firstOrNull { interval -> interval.id == selectedSingleIntervalId }

    val isConfirmEnabled: Boolean
        get() = when {
            selectedProducts.isEmpty() -> false
            isIntervalsLoading -> false
            isConfirmLoading -> false
            isSelectedPlaceWithoutAddress -> false
            deliveryMode == FittingConfirmationDeliveryMode.Single -> selectedSingleIntervalId != null
            isMultipleDeliveryAvailable -> {
                deliveryGroups.all { group -> selectedDeliveryIntervalIds[group.id] != null }
            }
            else -> selectedSingleIntervalId != null
        }
}
