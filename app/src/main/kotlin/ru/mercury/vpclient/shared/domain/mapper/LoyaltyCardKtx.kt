package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.LoyaltyCardDescription
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType
import ru.mercury.vpclient.shared.data.network.response.CardTypeResponse
import ru.mercury.vpclient.shared.data.network.response.CardInfoResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.LoyaltyCardInfoEntity

val CardInfoResponse.entity: LoyaltyCardInfoEntity
    get() = LoyaltyCardInfoEntity(
        loyaltyCardNumber = loyaltyCardNumber.orEmpty(),
        bonusAmount = bonusAmount?.toInt() ?: 0,
        clientName = clientName.orEmpty(),
        typeCard = typeCard.loyaltyCardType,
        qrCode = qrCode.orEmpty()
    )

val CardTypeResponse.loyaltyCardDescription: LoyaltyCardDescription
    get() = LoyaltyCardDescription(
        id = id ?: 0L,
        type = type.loyaltyCardType,
        cardName = cardName.orEmpty(),
        bonusRules = bonusRules.orEmpty(),
        termsForObtaining = termsForObtaining.orEmpty(),
        validity = validity.orEmpty(),
        renewalTerms = renewalTerms.orEmpty(),
        termsOfUse = termsOfUse.orEmpty()
    )

val LoyaltyCardType.order: Int
    get() = when (this) {
        LoyaltyCardType.Silver -> 0
        LoyaltyCardType.Gold -> 1
        LoyaltyCardType.Black -> 2
    }

private val String?.loyaltyCardType: LoyaltyCardType
    get() = when (this) {
        "silver" -> LoyaltyCardType.Silver
        "gold" -> LoyaltyCardType.Gold
        "black" -> LoyaltyCardType.Black
        else -> LoyaltyCardType.Black
    }
