package ru.mercury.vpclient.shared.domain.repository

import ru.mercury.vpclient.shared.data.entity.LoyaltyCardDescription
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardInfo

interface LoyaltyRepository {

    suspend fun linkLoyaltyCard(cardNumber: String)

    suspend fun verifyLoyaltyCard(cardNumber: String, code: String)

    suspend fun linkLoyaltyCardByPhone(phone: String): Boolean

    suspend fun verifyLoyaltyCardByPhone(phone: String, code: String)

    suspend fun loyaltyCardInfo(): LoyaltyCardInfo

    suspend fun loyaltyCardTypes(): List<LoyaltyCardDescription>

    suspend fun deleteLoyaltyCard(cardNumber: String)
}
