package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditPaletteFolderRequest(
    @SerialName("name") val name: String? = null
)
