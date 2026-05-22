package ru.mercury.vpclient.features.cart.model

data class CartEditProductAction(
    val text: String,
    val onClick: () -> Unit
)
