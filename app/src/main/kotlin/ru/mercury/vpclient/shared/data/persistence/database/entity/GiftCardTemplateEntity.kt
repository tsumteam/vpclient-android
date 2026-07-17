package ru.mercury.vpclient.shared.data.persistence.database.entity

import kotlinx.serialization.Serializable

@Serializable
data class GiftCardTemplateEntity(
    val id: Int = 0,
    val templateId: Int = 0,
    val name: String = "",
    val description: String = "",
    val photoUrl: String = "",
    val termOfUse: String = "",
    val orderView: Int = 0
) {
    companion object {
        val Empty = GiftCardTemplateEntity()
    }
}
