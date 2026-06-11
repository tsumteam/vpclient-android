package ru.mercury.vpclient.shared.data.entity

data class ProfileOrderDetailsProduct(
    val id: String,
    val productId: String,
    val imageUrl: String,
    val brand: String,
    val urlBrandLogo: String?,
    val name: String,
    val color: String,
    val article: String,
    val price: String,
    val size: String,
    val status: String,
    val quantity: Int
)
