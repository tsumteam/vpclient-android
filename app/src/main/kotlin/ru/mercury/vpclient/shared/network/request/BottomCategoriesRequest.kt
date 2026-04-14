package ru.mercury.vpclient.shared.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BottomCategoriesRequest(
    @SerialName("parentCategoryId") val parentCategoryId: Int
)
