package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.ClientClass

@Serializable
data class MyClientsClientResponse(
    @SerialName("clientEmail") val clientEmail: String? = null,
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("clientMiddleName") val clientMiddleName: String? = null,
    @SerialName("clientName") val clientName: String? = null,
    @SerialName("clientPhone") val clientPhone: String? = null,
    @SerialName("clientSurname") val clientSurname: String? = null,
    @SerialName("clientClass") val clientClass: ClientClass? = null,
    @SerialName("isRegistered") val isRegistered: Boolean? = null,
    @SerialName("lastActivity") val lastActivity: MyClientsClientLastActivityResponse? = null,
    @SerialName("isAvailableFittingHome") val isAvailableFittingHome: Boolean? = null,
    @SerialName("isReadyToConnect") val isReadyToConnect: Boolean? = null
)
