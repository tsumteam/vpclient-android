package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MyEmployeesIconTypeResponse {
    @SerialName("basket") BASKET,
    @SerialName("order") ORDER,
    @SerialName("fitting") FITTING,
    @SerialName("messenger") MESSENGER,
    @SerialName("compilation") COMPILATION
}
