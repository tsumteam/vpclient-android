package ru.mercury.vpclient.features.cart.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class CartRoute(
    val page: CartPage = CartPage.Cart
): NavKey

@Serializable
enum class CartPage {
    Cart,
    Fitting
}
