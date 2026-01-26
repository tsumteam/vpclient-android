package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CargoPackageReceiveRequest(
    @SerialName("cargoPackages") val cargoPackages: List<String>,
    @SerialName("emplBarcode") val emplBarcode: String,
    @SerialName("inventLocationId") val inventLocationId: String
)
