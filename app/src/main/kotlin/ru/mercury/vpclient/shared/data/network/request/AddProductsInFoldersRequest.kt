package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.response.PaletteProductRequestItemResponse

@Serializable
data class AddProductsInFoldersRequest(
    @SerialName("folderIds") val folderIds: List<Int>? = null,
    @SerialName("items") val items: List<PaletteProductRequestItemResponse>? = null
)
