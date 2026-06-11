package ru.mercury.vpclient.shared.domain.interactor

interface LoyaltyInteractor {

    suspend fun linkLoyaltyCard(cardNumber: String)

    suspend fun verifyLoyaltyCard(cardNumber: String, code: String)

    suspend fun linkLoyaltyCardByPhone(phone: String): Boolean

    suspend fun verifyLoyaltyCardByPhone(phone: String, code: String)
}
