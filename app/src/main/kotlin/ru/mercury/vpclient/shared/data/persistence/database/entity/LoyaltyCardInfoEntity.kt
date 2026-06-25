package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType

@Entity(
    tableName = "LoyaltyCardInfo",
    primaryKeys = ["id"]
)
data class LoyaltyCardInfoEntity(
    val id: Int = 0,
    val loyaltyCardNumber: String,
    val bonusAmount: Int,
    val clientName: String,
    val typeCard: LoyaltyCardType,
    val qrCode: String
) {
    companion object {
        val Empty = LoyaltyCardInfoEntity(
            loyaltyCardNumber = "",
            bonusAmount = 0,
            clientName = "",
            typeCard = LoyaltyCardType.Black,
            qrCode = ""
        )
    }
}
