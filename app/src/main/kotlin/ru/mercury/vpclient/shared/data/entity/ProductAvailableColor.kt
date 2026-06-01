package ru.mercury.vpclient.shared.data.entity

data class ProductAvailableColor(
    val id: String,
    val name: String,
    val hex: String,
    val selected: Boolean = false
)
