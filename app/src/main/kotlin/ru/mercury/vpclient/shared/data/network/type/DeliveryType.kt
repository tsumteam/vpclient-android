package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class DeliveryType {
    @SerialName("logistic") LOGISTIC,
    @SerialName("employee") EMPLOYEE
}
