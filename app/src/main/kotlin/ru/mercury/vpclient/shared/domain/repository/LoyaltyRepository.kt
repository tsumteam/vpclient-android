package ru.mercury.vpclient.shared.domain.repository

import ru.mercury.vpclient.shared.data.entity.LoyaltyCardDescription

interface LoyaltyRepository {

    suspend fun verifyLoyaltyCard(cardNumber: String, code: String)

    suspend fun linkLoyaltyCardByPhone(phone: String): Boolean

    suspend fun verifyLoyaltyCardByPhone(phone: String, code: String)

    suspend fun loyaltyCardTypes(): List<LoyaltyCardDescription>

    suspend fun deleteLoyaltyCard(cardNumber: String)
}
