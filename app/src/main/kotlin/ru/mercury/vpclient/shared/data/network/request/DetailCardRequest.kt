package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.Serializable

@Serializable
data class DetailCardRequest(
    val itemId: String? = null,
    val colorId: String? = null,
    val compilationLookProductId: Int? = null,
    val fashionImageId: Int? = null
)
