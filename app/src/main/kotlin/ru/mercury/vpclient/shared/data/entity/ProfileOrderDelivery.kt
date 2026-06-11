package ru.mercury.vpclient.shared.data.entity

data class ProfileOrderDelivery(
    val id: String,
    val date: String,
    val address: String,
    val products: List<ProfileOrderDetailsProduct>
)
