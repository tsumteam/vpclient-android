package ru.mercury.vpclient.features.cart.model

import ru.mercury.vpclient.shared.data.entity.CartProduct

data class CartProductGroup(
    val lookId: String?,
    val lookName: String,
    val lookImageUrl: String?,
    val products: List<CartProduct>
) {
    val key: String
        get() {
            return lookId ?: products.firstOrNull()?.id.orEmpty()
        }

    val isLook: Boolean
        get() {
            return !lookId.isNullOrEmpty()
        }
}
