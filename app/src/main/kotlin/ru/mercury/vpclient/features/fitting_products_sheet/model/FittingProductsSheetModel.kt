package ru.mercury.vpclient.features.fitting_products_sheet.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.CartProductEntity
import ru.mercury.vpclient.shared.mvi.Model

data class FittingProductsSheetModel(
    val cartProductEntities: List<CartProductEntity> = emptyList(),
    val selectedProductIds: Set<String> = emptySet()
): Model {

    val isConfirmEnabled: Boolean
        get() = selectedProductIds.isNotEmpty()
}
