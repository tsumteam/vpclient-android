package ru.mercury.vpclient.features.fitting_confirmation.model

import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddress
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.features.fitting_address_sheet.model.FittingAddressModel
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryMode
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationPlaceType
import ru.mercury.vpclient.shared.mvi.Model

data class FittingConfirmationModel(
    val route: FittingConfirmationRoute = FittingConfirmationRoute(
        productIds = emptyList()
    ),
    val products: List<CartProduct> = emptyList(),
    val boutiqueAddress: String? = null,
    val clientAddress: String? = null,
    val clientAddresses: List<ClientDeliveryAddress> = emptyList(),
    val selectedClientAddressId: Int? = null,
    val pendingClientAddressId: Int? = null,
    val addressActionAddressId: Int? = null,
    val deleteAddressId: Int? = null,
    val isAddressFormVisible: Boolean = false,
    val isAddressSearchVisible: Boolean = false,
    val addressForm: FittingAddressModel = FittingAddressModel(),
    val addressSuggestions: List<ClientDeliveryAddressSuggestion> = emptyList(),
    val isAddressListLoading: Boolean = false,
    val isAddressSuggestionsLoading: Boolean = false,
    val isAddressSaving: Boolean = false,
    val addressSearchQuery: String = "",
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
    val isIntervalsLoading: Boolean = false,
    val isConfirmLoading: Boolean = false,
    val intervalsError: String? = null
): Model {
    val selectedProducts: List<CartProduct>
        get() {
            return route.productIds.mapNotNull { productId ->
                products.firstOrNull { product -> product.id == productId }
            }
        }

    val selectedClientAddress: ClientDeliveryAddress?
        get() {
            return clientAddresses.firstOrNull { address -> address.id == selectedClientAddressId }
        }

    val pendingClientAddress: ClientDeliveryAddress?
        get() {
            return clientAddresses.firstOrNull { address -> address.id == pendingClientAddressId }
        }

    val addressActionAddress: ClientDeliveryAddress?
        get() {
            return clientAddresses.firstOrNull { address -> address.id == addressActionAddressId }
        }

    val deleteAddress: ClientDeliveryAddress?
        get() {
            return clientAddresses.firstOrNull { address -> address.id == deleteAddressId }
        }

    val displayedClientAddress: String?
        get() {
            return selectedClientAddress?.title ?: clientAddress
        }

    val isSelectedPlaceWithoutAddress: Boolean
        get() {
            return when (selectedPlaceType) {
                FittingConfirmationPlaceType.Boutique -> false
                FittingConfirmationPlaceType.Home -> displayedClientAddress.isNullOrBlank()
                FittingConfirmationPlaceType.Other -> true
            }
        }

    val isMultipleDeliveryAvailable: Boolean
        get() {
            return deliveryGroups.size > 1
        }

    val selectedSingleDayIntervals: List<FittingConfirmationDeliveryInterval>
        get() {
            val selectedDayId = selectedSingleDayId ?: singleIntervals.firstOrNull()?.dayId
            return singleIntervals.filter { interval -> interval.dayId == selectedDayId }
        }

    val selectedSingleInterval: FittingConfirmationDeliveryInterval?
        get() {
            return singleIntervals.firstOrNull { interval -> interval.id == selectedSingleIntervalId }
        }

    val isConfirmEnabled: Boolean
        get() {
            return when {
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
}
