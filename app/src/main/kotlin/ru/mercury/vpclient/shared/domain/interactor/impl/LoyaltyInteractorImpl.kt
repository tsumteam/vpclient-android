package ru.mercury.vpclient.shared.domain.interactor.impl

import kotlinx.coroutines.withContext
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.domain.interactor.LoyaltyInteractor
import ru.mercury.vpclient.shared.domain.repository.LoyaltyRepository
import javax.inject.Inject

class LoyaltyInteractorImpl @Inject constructor(
    private val dispatchers: SharedDispatchers,
    private val loyaltyRepository: LoyaltyRepository
): LoyaltyInteractor {

    override suspend fun linkLoyaltyCard(cardNumber: String) {
        withContext(dispatchers.io) { loyaltyRepository.linkLoyaltyCard(cardNumber = cardNumber) }
    }

    override suspend fun verifyLoyaltyCard(cardNumber: String, code: String) {
        withContext(dispatchers.io) {
            loyaltyRepository.verifyLoyaltyCard(
                cardNumber = cardNumber,
                code = code
            )
        }
    }

    override suspend fun linkLoyaltyCardByPhone(phone: String): Boolean {
        return withContext(dispatchers.io) {
            loyaltyRepository.linkLoyaltyCardByPhone(phone = phone)
        }
    }

    override suspend fun verifyLoyaltyCardByPhone(phone: String, code: String) {
        withContext(dispatchers.io) {
            loyaltyRepository.verifyLoyaltyCardByPhone(
                phone = phone,
                code = code
            )
        }
    }
}
