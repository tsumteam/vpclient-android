package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity
import ru.mercury.vpclient.shared.data.network.type.GiftCardType

@Entity(
    tableName = "GiftCard",
    primaryKeys = ["id"]
)
data class GiftCardEntity(
    val id: Int,
    val itemId: String,
    val type: GiftCardType,
    val maxAmount: Int,
    val minAmount: Int,
    val defaultAmount: Int,
    val presetAmounts: List<Int>,
    val templates: List<GiftCardTemplateEntity>
) {
    companion object {
        val Empty = GiftCardEntity(
            id = 0,
            itemId = "",
            type = GiftCardType.NONE,
            maxAmount = 0,
            minAmount = 0,
            defaultAmount = 0,
            presetAmounts = emptyList(),
            templates = emptyList()
        )
    }
}
