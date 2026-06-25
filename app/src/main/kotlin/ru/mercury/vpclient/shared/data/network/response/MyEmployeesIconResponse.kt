package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.MyEmployeesIconTypeResponse

@Serializable
data class MyEmployeesIconResponse(
    @SerialName("colorHex") val colorHex: String? = null,
    @SerialName("isActive") val isActive: Boolean? = null,
    @SerialName("number") val number: Int? = null,
    @SerialName("type") val type: MyEmployeesIconTypeResponse? = null
)
