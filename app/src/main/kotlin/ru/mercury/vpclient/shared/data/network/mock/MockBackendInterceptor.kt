package ru.mercury.vpclient.shared.data.network.mock

import android.content.Context
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import ru.mercury.vpclient.BuildConfig
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import java.io.File
import javax.inject.Provider

class MockBackendInterceptor(
    private val context: Context,
    private val settingsDataStore: Provider<SettingsDataStore>
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val enabled = BuildConfig.DEBUG &&
            (context.isMockBackendMarkerEnabled || settingsDataStore.get().getValueBlocking(PreferenceKey.MockBackendEnabled) == true)
        if (!enabled || !request.isMockBackendRequest) {
            return chain.proceed(request)
        }

        when (request.mockEndpoint) {
            LOYALTY_LINK_ENDPOINT -> context.pendingMockLoyaltyCardNumber = request.loyaltyCardNumberFromBody
            LOYALTY_VERIFY_LINK_ENDPOINT -> context.linkMockLoyaltyCard(
                cardNumber = request.loyaltyCardNumberFromBody
                    ?: context.pendingMockLoyaltyCardNumber
                    ?: DEFAULT_MOCK_LOYALTY_CARD_NUMBER
            )
            LOYALTY_LINK_BY_PHONE_CONTINUE_ENDPOINT -> context.linkMockLoyaltyCard(
                cardNumber = DEFAULT_MOCK_LOYALTY_CARD_NUMBER
            )
        }

        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .header("Content-Type", CONTENT_TYPE_JSON)
            .body(request.mockResponseBody.toResponseBody(CONTENT_TYPE_JSON.toMediaType()))
            .build()
    }

    private val Context.isMockBackendMarkerEnabled: Boolean
        get() {
            return File(filesDir, MOCK_BACKEND_MARKER_FILE_NAME).exists()
        }

    private var Context.pendingMockLoyaltyCardNumber: String?
        get() {
            val file = File(filesDir, PENDING_MOCK_LOYALTY_CARD_FILE_NAME)
            return file.takeIf(File::exists)?.readText()?.takeIf(String::isNotBlank)
        }
        set(value) {
            val file = File(filesDir, PENDING_MOCK_LOYALTY_CARD_FILE_NAME)
            when {
                value.isNullOrBlank() -> file.delete()
                else -> file.writeText(value)
            }
        }

    private val Context.mockLoyaltyCardNumber: String?
        get() {
            val file = File(filesDir, MOCK_LOYALTY_CARD_FILE_NAME)
            return file.takeIf(File::exists)?.readText()?.takeIf(String::isNotBlank)
        }

    private fun Context.linkMockLoyaltyCard(
        cardNumber: String
    ) {
        File(filesDir, MOCK_LOYALTY_CARD_FILE_NAME).writeText(cardNumber)
        pendingMockLoyaltyCardNumber = null
    }

    private val okhttp3.Request.isMockBackendRequest: Boolean
        get() {
            return mockEndpoint in MOCK_BACKEND_ENDPOINTS
        }

    private val okhttp3.Request.mockEndpoint: String
        get() {
            return url.encodedPath
                .trim('/')
                .removePrefix("$API_PATH_PREFIX/")
        }

    private val okhttp3.Request.bodyText: String
        get() {
            val buffer = Buffer()
            body?.writeTo(buffer)
            return buffer.readUtf8()
        }

    private val okhttp3.Request.loyaltyCardNumberFromBody: String?
        get() {
            val jsonObject = runCatching {
                Json.parseToJsonElement(bodyText).jsonObject
            }.getOrNull() ?: return null
            return jsonObject["loyaltyCardNumber"]?.jsonPrimitive?.contentOrNull
        }

    private val okhttp3.Request.mockResponseBody: String
        get() {
            return when (mockEndpoint) {
                LOYALTY_LINK_BY_PHONE_ENDPOINT -> LOYALTY_LINK_BY_PHONE_RESPONSE
                LOYALTY_CARD_INFO_ENDPOINT -> context.mockLoyaltyCardNumber?.let(::loyaltyCardInfoResponse) ?: EMPTY_LOYALTY_CARD_INFO_RESPONSE
                else -> LOYALTY_OPERATION_RESPONSE
            }
        }

    private companion object {
        const val CONTENT_TYPE_JSON = "application/json"
        const val API_PATH_PREFIX = "api"
        const val MOCK_BACKEND_MARKER_FILE_NAME = "mock-backend-enabled"
        const val PENDING_MOCK_LOYALTY_CARD_FILE_NAME = "pending-mock-loyalty-card-number"
        const val MOCK_LOYALTY_CARD_FILE_NAME = "mock-loyalty-card-number"
        const val DEFAULT_MOCK_LOYALTY_CARD_NUMBER = "G40135"
        const val LOYALTY_LINK_ENDPOINT = "loyalty/link"
        const val LOYALTY_VERIFY_LINK_ENDPOINT = "loyalty/verify-link"
        const val LOYALTY_LINK_BY_PHONE_ENDPOINT = "loyalty/link-by-phone"
        const val LOYALTY_LINK_BY_PHONE_CONTINUE_ENDPOINT = "loyalty/link-by-phone-continue"
        const val LOYALTY_CARD_INFO_ENDPOINT = "loyalty/card-info"
        const val LOYALTY_OPERATION_RESPONSE = """{"data":{"error":null},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        const val LOYALTY_LINK_BY_PHONE_RESPONSE = """{"data":{"isNeedVerification":true,"error":null},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        const val EMPTY_LOYALTY_CARD_INFO_RESPONSE = """{"data":{"loyaltyCardNumber":"","bonusAmount":0,"clientName":"","typeCard":"black","qrCode":""},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""

        val MOCK_BACKEND_ENDPOINTS = setOf(
            LOYALTY_LINK_ENDPOINT,
            LOYALTY_VERIFY_LINK_ENDPOINT,
            LOYALTY_LINK_BY_PHONE_ENDPOINT,
            LOYALTY_LINK_BY_PHONE_CONTINUE_ENDPOINT,
            LOYALTY_CARD_INFO_ENDPOINT
        )

        fun loyaltyCardInfoResponse(cardNumber: String): String {
            return """{"data":{"loyaltyCardNumber":"$cardNumber","bonusAmount":1500,"clientName":"Mock Client","typeCard":"black","qrCode":"$cardNumber"},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        }
    }
}
