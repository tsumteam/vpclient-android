package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.MyClientsIconTypeResponse

@Serializable
data class MyClientsIconResponse(
    @SerialName("colorHex") val colorHex: String? = null,
    @SerialName("isActive") val isActive: Boolean? = null,
    @SerialName("number") val number: Int? = null,
    @SerialName("secondNumber") val secondNumber: Int? = null,
    @SerialName("type") val type: MyClientsIconTypeResponse? = null
)
