package ru.mercury.vpclient.features.cart_fitting_products_sheet.model

import ru.mercury.vpclient.shared.data.entity.CartProduct

data class CartFittingProductsSheetModel(
    val products: List<CartProduct>,
    val selectedProductIds: Set<String> = emptySet()
)
