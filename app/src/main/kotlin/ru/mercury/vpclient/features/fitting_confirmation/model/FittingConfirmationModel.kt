package ru.mercury.vpclient.features.fitting_confirmation.model

import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.mvi.Model

data class FittingConfirmationModel(
    val route: FittingConfirmationRoute,
    val products: List<CartProduct> = emptyList(),
    val selectedPlaceIndex: Int? = null,
    val selectedDayIndex: Int = 0,
    val selectedInterval: String? = null
): Model {
    val selectedProducts: List<CartProduct>
        get() {
            return route.productIds.mapNotNull { productId ->
                products.firstOrNull { product -> product.id == productId }
            }
        }

    val isConfirmEnabled: Boolean
        get() {
            return selectedPlaceIndex != null && selectedInterval != null
        }
}
