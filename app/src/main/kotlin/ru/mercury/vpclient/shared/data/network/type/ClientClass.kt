package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ClientClass {
    @SerialName("vip3.5") VIP_3_5,
    @SerialName("vip4") VIP_4,
    @SerialName("vip3") VIP_3,
    @SerialName("vip2") VIP_2,
    @SerialName("vip1") VIP_1,
    @SerialName("photo") PHOTO
}
