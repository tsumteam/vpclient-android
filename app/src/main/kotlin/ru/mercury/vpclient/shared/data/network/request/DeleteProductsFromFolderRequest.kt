package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.response.PaletteProductRequestItemResponse

@Serializable
data class DeleteProductsFromFolderRequest(
    @SerialName("items") val items: List<PaletteProductRequestItemResponse>? = null
)
