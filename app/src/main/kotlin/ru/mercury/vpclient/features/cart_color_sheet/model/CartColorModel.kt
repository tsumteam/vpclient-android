package ru.mercury.vpclient.features.cart_color_sheet.model

import ru.mercury.vpclient.shared.data.entity.ProductAvailableColor

data class CartColorModel(
    val colors: List<ProductAvailableColor>
) {
    val hasSelectedColor: Boolean
        get() = colors.any { it.selected }
}
