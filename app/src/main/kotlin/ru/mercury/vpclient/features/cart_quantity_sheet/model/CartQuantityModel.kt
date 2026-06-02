package ru.mercury.vpclient.features.cart_quantity_sheet.model

data class CartQuantityModel(
    val quantities: List<CartQuantityItem>
) {
    val hasSelectedQuantity: Boolean
        get() {
            return quantities.any { it.selected }
        }
}

data class CartQuantityItem(
    val value: Int,
    val selected: Boolean = false
)
