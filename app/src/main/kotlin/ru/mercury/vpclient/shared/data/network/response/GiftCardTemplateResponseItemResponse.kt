package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GiftCardTemplateResponseItemResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("templateId") val templateId: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("termOfUse") val termOfUse: String? = null,
    @SerialName("orderView") val orderView: Int? = null
)
