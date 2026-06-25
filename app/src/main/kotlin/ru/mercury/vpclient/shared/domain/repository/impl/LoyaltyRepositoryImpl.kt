package ru.mercury.vpclient.shared.domain.repository.impl

import ru.mercury.vpclient.shared.data.FORMAT_PHONE_NUMBER
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardDescription
import ru.mercury.vpclient.shared.data.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.DeleteLinkedCardRequest
import ru.mercury.vpclient.shared.data.network.request.VerifyLinkCardRequest
import ru.mercury.vpclient.shared.data.network.request.LoyaltyLinkByPhoneRequest
import ru.mercury.vpclient.shared.data.network.request.LoyaltyVerifyByPhoneRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.loyaltyCardDescription
import ru.mercury.vpclient.shared.domain.repository.LoyaltyRepository
import java.util.Locale
import javax.inject.Inject

class LoyaltyRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): LoyaltyRepository {

    override suspend fun verifyLoyaltyCard(cardNumber: String, code: String) {
        val response = handleResponseResult {
            val request = VerifyLinkCardRequest(
                loyaltyCardNumber = cardNumber,
                smsCode = code
            )
            networkService.loyaltyVerifyLink(request = request)
        }.getOrThrow()

        response.error?.let { error ->
            throw ClientException((error.display ?: error.msg).orEmpty().ifEmpty { "Неверный код" })
        }
    }

    override suspend fun linkLoyaltyCardByPhone(phone: String): Boolean {
        val response = handleResponseResult {
            val request = LoyaltyLinkByPhoneRequest(
                clientPhone = String.format(Locale.getDefault(), FORMAT_PHONE_NUMBER, phone)
            )
            networkService.loyaltyLinkByPhone(request = request)
        }.getOrThrow()

        response.error?.let { error ->
            throw ClientException((error.display ?: error.msg).orEmpty().ifEmpty { "Не удалось привязать карту" })
        }

        return response.isNeedVerification ?: throw ClientException("Не удалось привязать карту")
    }

    override suspend fun verifyLoyaltyCardByPhone(phone: String, code: String) {
        val response = handleResponseResult {
            val request = LoyaltyVerifyByPhoneRequest(
                phone = String.format(Locale.getDefault(), FORMAT_PHONE_NUMBER, phone),
                code = code
            )
            networkService.loyaltyVerifyByPhone(request = request)
        }.getOrThrow()

        response.error?.let { error ->
            throw ClientException((error.display ?: error.msg).orEmpty().ifEmpty { "Неверный код" })
        }
    }

    override suspend fun loyaltyCardTypes(): List<LoyaltyCardDescription> {
        return handleResponseResult {
            networkService.loyaltyCardTypes()
        }.getOrThrow()
            .items
            .orEmpty()
            .map { cardType -> cardType.loyaltyCardDescription }
    }

    override suspend fun deleteLoyaltyCard(cardNumber: String) {
        handleResponseResult {
            val request = DeleteLinkedCardRequest(loyaltyCardNumber = cardNumber)
            networkService.loyaltyDelete(request = request)
        }.getOrThrow()
    }
}
