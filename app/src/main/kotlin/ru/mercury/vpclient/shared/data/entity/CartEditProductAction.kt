package ru.mercury.vpclient.shared.data.entity

data class CartEditProductAction(
    val text: String,
    val onClick: () -> Unit
)