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
        get() = File(filesDir, MOCK_BACKEND_MARKER_FILE_NAME).exists()

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
        get() = mockEndpoint in MOCK_BACKEND_ENDPOINTS ||
            mockEndpoint.isActivityCountersEndpoint ||
            mockEndpoint.isEmployeeBadgesEndpoint ||
            mockEndpoint.isEmployeeEndpoint

    private val okhttp3.Request.mockEndpoint: String
        get() = url.encodedPath
                .trim('/')
                .removePrefix("$API_PATH_PREFIX/")

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
        get() = when (mockEndpoint) {
            AUTHENTICATION_REGISTER_ENDPOINT -> AUTHENTICATION_RESPONSE
            AUTHENTICATION_LOGIN_ENDPOINT -> AUTHENTICATION_RESPONSE
            AUTHENTICATION_CONTINUE_LOGIN_ENDPOINT -> TOKEN_RESPONSE
            USER_CURRENT_USER_ENDPOINT -> CURRENT_USER_RESPONSE
            CLIENT_MY_EMPLOYEES_ENDPOINT -> MY_EMPLOYEES_RESPONSE
            CLIENT_ACTIVE_EMPLOYEE_ENDPOINT -> activeEmployeeResponse(context.activeMockEmployeeId)
            LOYALTY_LINK_BY_PHONE_ENDPOINT -> LOYALTY_LINK_BY_PHONE_RESPONSE
            LOYALTY_CARD_INFO_ENDPOINT -> context.mockLoyaltyCardNumber?.let(::loyaltyCardInfoResponse) ?: EMPTY_LOYALTY_CARD_INFO_RESPONSE
            else -> when {
                mockEndpoint.isActivityCountersEndpoint -> ACTIVITY_COUNTERS_RESPONSE
                mockEndpoint.isEmployeeBadgesEndpoint -> employeeBadgesResponse(mockEndpoint.employeeIdFromBadgesEndpoint)
                mockEndpoint.isEmployeeEndpoint -> activeEmployeeResponse(mockEndpoint.employeeIdFromEmployeeEndpoint)
                else -> LOYALTY_OPERATION_RESPONSE
            }
        }

    private val Context.activeMockEmployeeId: String
        get() = settingsDataStore.get().getValueBlocking(PreferenceKey.PairedUser)
            ?.takeIf(String::isNotBlank)
            ?: DEFAULT_MOCK_EMPLOYEE_ID

    private val String.isEmployeeBadgesEndpoint: Boolean
        get() = startsWith("$CLIENT_MY_EMPLOYEES_ENDPOINT/") && endsWith("/badges")

    private val String.employeeIdFromBadgesEndpoint: String
        get() = removePrefix("$CLIENT_MY_EMPLOYEES_ENDPOINT/")
            .removeSuffix("/badges")

    private val String.isActivityCountersEndpoint: Boolean
        get() = startsWith("$ACTIVITY_COUNTERS_ENDPOINT/")

    private val String.isEmployeeEndpoint: Boolean
        get() = startsWith("$CLIENT_MY_EMPLOYEE_ENDPOINT/")

    private val String.employeeIdFromEmployeeEndpoint: String
        get() = removePrefix("$CLIENT_MY_EMPLOYEE_ENDPOINT/")

    private companion object {
        const val CONTENT_TYPE_JSON = "application/json"
        const val API_PATH_PREFIX = "api"
        const val MOCK_BACKEND_MARKER_FILE_NAME = "mock-backend-enabled"
        const val PENDING_MOCK_LOYALTY_CARD_FILE_NAME = "pending-mock-loyalty-card-number"
        const val MOCK_LOYALTY_CARD_FILE_NAME = "mock-loyalty-card-number"
        const val DEFAULT_MOCK_LOYALTY_CARD_NUMBER = "G40135"
        const val DEFAULT_MOCK_EMPLOYEE_ID = "MOCK_EMPLOYEE_1"
        const val AUTHENTICATION_REGISTER_ENDPOINT = "authentication/register"
        const val AUTHENTICATION_LOGIN_ENDPOINT = "authentication/login"
        const val AUTHENTICATION_CONTINUE_LOGIN_ENDPOINT = "authentication/continue-login"
        const val USER_CURRENT_USER_ENDPOINT = "user/current-user"
        const val CLIENT_MY_EMPLOYEES_ENDPOINT = "client/my-employees"
        const val CLIENT_MY_EMPLOYEE_ENDPOINT = "client/my-employee"
        const val CLIENT_ACTIVE_EMPLOYEE_ENDPOINT = "client/active-employee"
        const val ACTIVITY_COUNTERS_ENDPOINT = "activity/counters"
        const val LOYALTY_LINK_ENDPOINT = "loyalty/link"
        const val LOYALTY_VERIFY_LINK_ENDPOINT = "loyalty/verify-link"
        const val LOYALTY_LINK_BY_PHONE_ENDPOINT = "loyalty/link-by-phone"
        const val LOYALTY_LINK_BY_PHONE_CONTINUE_ENDPOINT = "loyalty/link-by-phone-continue"
        const val LOYALTY_CARD_INFO_ENDPOINT = "loyalty/card-info"
        const val AUTHENTICATION_RESPONSE = """{"data":"ok","error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        const val TOKEN_RESPONSE = """{"data":{"token":"mock-user-token"},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        const val CURRENT_USER_RESPONSE = """{"data":{"clientEmail":"mock.client@example.com","clientMiddleName":"","clientName":"Мок","clientPhone":"+79990000000","clientSurname":"Клиент","isAvailableFittingHome":true,"code":"MOCK_CLIENT_1","createDate":"2026-01-01T00:00:00Z","deviceId":"mock-device","disableCommunications":false,"gender":"masculine","id":1,"isBoutique":false,"isEmployee":false,"isRegistered":true,"useDiginetica":false},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        const val ACTIVITY_COUNTERS_RESPONSE = """{"data":{"items":[{"type":"messages","value":7},{"type":"basket","value":2},{"type":"order","value":1},{"type":"fitting","value":1},{"type":"compilation","value":3},{"type":"clientNotification","value":1}]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        const val LOYALTY_OPERATION_RESPONSE = """{"data":{"error":null},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        const val LOYALTY_LINK_BY_PHONE_RESPONSE = """{"data":{"isNeedVerification":true,"error":null},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        const val EMPTY_LOYALTY_CARD_INFO_RESPONSE = """{"data":{"loyaltyCardNumber":"","bonusAmount":0,"clientName":"","typeCard":"black","qrCode":""},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""

        val MOCK_BACKEND_ENDPOINTS = setOf(
            AUTHENTICATION_REGISTER_ENDPOINT,
            AUTHENTICATION_LOGIN_ENDPOINT,
            AUTHENTICATION_CONTINUE_LOGIN_ENDPOINT,
            USER_CURRENT_USER_ENDPOINT,
            CLIENT_MY_EMPLOYEES_ENDPOINT,
            CLIENT_ACTIVE_EMPLOYEE_ENDPOINT,
            LOYALTY_LINK_ENDPOINT,
            LOYALTY_VERIFY_LINK_ENDPOINT,
            LOYALTY_LINK_BY_PHONE_ENDPOINT,
            LOYALTY_LINK_BY_PHONE_CONTINUE_ENDPOINT,
            LOYALTY_CARD_INFO_ENDPOINT
        )

        val MOCK_EMPLOYEES = listOf(
            mockEmployee(
                employeeId = "MOCK_EMPLOYEE_1",
                name = "Екатерина",
                surname = "Орлова",
                brand = "Brioni",
                boutique = "Петровка, 2",
                colorHex = "#2E7D32"
            ),
            mockEmployee(
                employeeId = "MOCK_EMPLOYEE_2",
                name = "Анна",
                surname = "Смирнова",
                brand = "BORK",
                boutique = "Барвиха Luxury Village",
                colorHex = "#1565C0"
            ),
            mockEmployee(
                employeeId = "MOCK_EMPLOYEE_3",
                name = "Мария",
                surname = "Соколова",
                brand = "MVST",
                boutique = "ЦУМ",
                colorHex = "#6A1B9A"
            ),
            mockEmployee(
                employeeId = "MOCK_EMPLOYEE_4",
                name = "Ирина",
                surname = "Петрова",
                brand = "VIP room",
                boutique = "Кутузовский",
                colorHex = "#C62828"
            ),
            mockEmployee(
                employeeId = "MOCK_EMPLOYEE_5",
                name = "Ольга",
                surname = "Волкова",
                brand = "Tiffany",
                boutique = "Столешников",
                colorHex = "#00897B"
            )
        )

        val MY_EMPLOYEES_RESPONSE = """{"data":{"items":[${MOCK_EMPLOYEES.joinToString(",")}]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""

        fun activeEmployeeResponse(employeeId: String): String {
            val employee = MOCK_EMPLOYEES.firstOrNull { employee -> employee.contains(""""employeeId":"$employeeId"""") }
                ?: MOCK_EMPLOYEES.first()
            return """{"data":$employee,"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        }

        fun employeeBadgesResponse(employeeId: String): String {
            val index = MOCK_EMPLOYEES.indexOfFirst { employee -> employee.contains(""""employeeId":"$employeeId"""") }
                .takeIf { index -> index >= 0 }
                ?: 0
            val basketBadge = index
            val fittingBadge = index + 1
            val messengerBadge = when (index) {
                0 -> 4
                1 -> 2
                2 -> 0
                else -> 1
            }
            val orderBadge = when (index) {
                2 -> 3
                else -> 0
            }
            val compilationBadge = when (index) {
                3 -> 5
                else -> index
            }
            return """{"data":{"employeeId":"$employeeId","basketIcon":${badge("basket", basketBadge, index + 1)},"fittingIcon":${badge("fitting", fittingBadge, index + 2)},"messengerIcon":${badge("messenger", messengerBadge, 0)},"orderIcon":${badge("order", orderBadge, 0)},"compilationIcon":${badge("compilation", compilationBadge, 0)}},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        }

        fun badge(type: String, badge: Int, number: Int): String {
            return """{"badge":$badge,"icon":{"colorHex":"#111111","isActive":${badge > 0},"number":$number,"type":"$type"}}"""
        }

        fun mockEmployee(
            employeeId: String,
            name: String,
            surname: String,
            brand: String,
            boutique: String,
            colorHex: String
        ): String {
            return """{"employeeEmail":"$employeeId@example.com","employeeId":"$employeeId","employeeMiddleName":"","employeeName":"$name","employeePhone":"+79990000000","employeeSurname":"$surname","photoUrl":"","previewPhotoUrl":"","lastActivity":{"colorHex":"$colorHex","date":"2026-06-25T10:00:00Z"},"employeeBotiqueAddress":"$boutique","employeeBotiqueAddressShort":"$boutique","employeeBrand":"$brand"}"""
        }

        fun loyaltyCardInfoResponse(cardNumber: String): String {
            return """{"data":{"loyaltyCardNumber":"$cardNumber","bonusAmount":1500,"clientName":"Mock Client","typeCard":"black","qrCode":"$cardNumber"},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        }
    }
}
