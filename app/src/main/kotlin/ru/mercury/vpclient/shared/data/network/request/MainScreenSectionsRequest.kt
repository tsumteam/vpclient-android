package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.MainScreenCategoryType

@Serializable
data class MainScreenSectionsRequest(
    @SerialName("category") val category: MainScreenCategoryType
)
