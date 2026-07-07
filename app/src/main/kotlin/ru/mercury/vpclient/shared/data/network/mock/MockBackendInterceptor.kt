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
        val enabled = BuildConfig.DEBUG && context.isMockBackendMarkerEnabled
        if (!enabled) return chain.proceed(request)

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
            COMPILATIONS_CLIENT_ENDPOINT -> COMPILATIONS_CLIENT_RESPONSE
            BASKET_ENDPOINT -> BASKET_OPERATION_RESPONSE
            LOYALTY_LINK_BY_PHONE_ENDPOINT -> LOYALTY_LINK_BY_PHONE_RESPONSE
            LOYALTY_CARD_INFO_ENDPOINT -> context.mockLoyaltyCardNumber?.let(::loyaltyCardInfoResponse) ?: EMPTY_LOYALTY_CARD_INFO_RESPONSE
            else -> when {
                mockEndpoint.isCompilationsClientByCompilationIdEndpoint -> compilationsClientByCompilationIdResponse(
                    mockEndpoint.compilationIdFromEndpoint
                )
                mockEndpoint.isCompilationsClientLookByLookIdEndpoint -> compilationsClientLookByLookIdResponse(
                    mockEndpoint.lookIdFromEndpoint
                )
                mockEndpoint.isCompilationsClientLookByLookIdToBasketEndpoint -> TRUE_RESPONSE
                mockEndpoint.isBasketByPairedUserIdEndpoint -> basketResponse()
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

    private val String.isCompilationsClientByCompilationIdEndpoint: Boolean
        get() = startsWith("$COMPILATIONS_CLIENT_ENDPOINT/") &&
            !startsWith("$COMPILATIONS_CLIENT_LOOK_ENDPOINT/")

    private val String.compilationIdFromEndpoint: Int
        get() = removePrefix("$COMPILATIONS_CLIENT_ENDPOINT/")
            .toIntOrNull()
            ?: DEFAULT_MOCK_COMPILATION_ID

    private val String.isCompilationsClientLookByLookIdEndpoint: Boolean
        get() = startsWith("$COMPILATIONS_CLIENT_LOOK_ENDPOINT/") &&
            !endsWith(COMPILATIONS_CLIENT_LOOK_TO_BASKET_SUFFIX)

    private val String.isCompilationsClientLookByLookIdToBasketEndpoint: Boolean
        get() = startsWith("$COMPILATIONS_CLIENT_LOOK_ENDPOINT/") &&
            endsWith(COMPILATIONS_CLIENT_LOOK_TO_BASKET_SUFFIX)

    private val String.lookIdFromEndpoint: Int
        get() = removePrefix("$COMPILATIONS_CLIENT_LOOK_ENDPOINT/")
            .removeSuffix(COMPILATIONS_CLIENT_LOOK_TO_BASKET_SUFFIX)
            .toIntOrNull()
            ?: DEFAULT_MOCK_LOOK_ID

    private val String.isBasketByPairedUserIdEndpoint: Boolean
        get() = startsWith("$BASKET_ENDPOINT/") && count { char -> char == '/' } == 1

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
        const val BASKET_ENDPOINT = "basket"
        const val COMPILATIONS_CLIENT_ENDPOINT = "compilations/client"
        const val COMPILATIONS_CLIENT_LOOK_ENDPOINT = "compilations/client/look"
        const val COMPILATIONS_CLIENT_LOOK_TO_BASKET_SUFFIX = "/to-basket"
        const val LOYALTY_LINK_ENDPOINT = "loyalty/link"
        const val LOYALTY_VERIFY_LINK_ENDPOINT = "loyalty/verify-link"
        const val LOYALTY_LINK_BY_PHONE_ENDPOINT = "loyalty/link-by-phone"
        const val LOYALTY_LINK_BY_PHONE_CONTINUE_ENDPOINT = "loyalty/link-by-phone-continue"
        const val LOYALTY_CARD_INFO_ENDPOINT = "loyalty/card-info"
        const val DEFAULT_MOCK_COMPILATION_ID = 103
        const val DEFAULT_MOCK_LOOK_ID = 10301
        const val AUTHENTICATION_RESPONSE = """{"data":"ok","error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        const val TOKEN_RESPONSE = """{"data":{"token":"mock-user-token"},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        const val CURRENT_USER_RESPONSE = """{"data":{"clientEmail":"mock.client@example.com","clientMiddleName":"","clientName":"Мок","clientPhone":"+79990000000","clientSurname":"Клиент","isAvailableFittingHome":true,"code":"MOCK_CLIENT_1","createDate":"2026-01-01T00:00:00Z","deviceId":"mock-device","disableCommunications":false,"gender":"masculine","id":1,"isBoutique":false,"isEmployee":false,"isRegistered":true,"useDiginetica":false},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        const val ACTIVITY_COUNTERS_RESPONSE = """{"data":{"items":[{"type":"messages","value":7},{"type":"basket","value":2},{"type":"order","value":1},{"type":"fitting","value":1},{"type":"compilation","value":3},{"type":"clientNotification","value":1}]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        const val TRUE_RESPONSE = """{"data":true,"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        const val BASKET_OPERATION_RESPONSE = """{"data":{},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
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
            COMPILATIONS_CLIENT_ENDPOINT,
            BASKET_ENDPOINT,
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

        const val COMPILATIONS_CLIENT_RESPONSE = """{"data":{"items":[{"badge":1,"id":101,"collageUrl":"https://picsum.photos/seed/vp-client-compilation-1/300/450","photoUrl":"https://picsum.photos/seed/vp-client-compilation-1/300/450","name":"Новые поступления","description":"Свежие образы недели","createDate":"2026-07-11T10:00:00Z","looksQty":5,"lookProductsQty":36,"isStatsAvailable":false},{"badge":1,"id":102,"collageUrl":"https://picsum.photos/seed/vp-client-compilation-2/300/450","photoUrl":"https://picsum.photos/seed/vp-client-compilation-2/300/450","name":"Sporty&Riche","description":"Комфортные комплекты для города","createDate":"2026-07-05T10:00:00Z","looksQty":4,"lookProductsQty":28,"isStatsAvailable":false},{"badge":0,"id":103,"collageUrl":"https://picsum.photos/seed/vp-client-compilation-3/300/450","photoUrl":"https://picsum.photos/seed/vp-client-compilation-3/300/450","name":"BLV/Hotel","description":"Капсула для путешествия","createDate":"2026-06-17T10:00:00Z","looksQty":5,"lookProductsQty":36,"isStatsAvailable":false},{"badge":0,"id":104,"collageUrl":"https://picsum.photos/seed/vp-client-compilation-4/300/450","photoUrl":"https://picsum.photos/seed/vp-client-compilation-4/300/450","name":"Летние образы","description":"Легкие комплекты для жарких дней","createDate":"2026-06-07T10:00:00Z","looksQty":8,"lookProductsQty":45,"isStatsAvailable":false},{"badge":1,"id":105,"collageUrl":"https://picsum.photos/seed/vp-client-compilation-5/300/450","photoUrl":"https://picsum.photos/seed/vp-client-compilation-5/300/450","name":"Деловая поездка","description":"Вещи для встреч и ужинов","createDate":"2026-05-28T10:00:00Z","looksQty":6,"lookProductsQty":31,"isStatsAvailable":false},{"badge":0,"id":106,"collageUrl":"https://picsum.photos/seed/vp-client-compilation-6/300/450","photoUrl":"https://picsum.photos/seed/vp-client-compilation-6/300/450","name":"Вечерний выход","description":"Акцентные образы для мероприятий","createDate":"2026-05-18T10:00:00Z","looksQty":3,"lookProductsQty":19,"isStatsAvailable":false}]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""

        val MY_EMPLOYEES_RESPONSE = """{"data":{"items":[${MOCK_EMPLOYEES.joinToString(",")}]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""

        fun compilationsClientByCompilationIdResponse(compilationId: Int): String {
            val looks = (1..5).joinToString(",") { index ->
                lookInfo(
                    lookId = compilationId * 100 + index
                )
            }
            return """{"data":{"compilationInfo":${compilationInfo(compilationId)},"looks":[$looks]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        }

        fun compilationsClientLookByLookIdResponse(lookId: Int): String {
            val products = (1..6).joinToString(",") { index ->
                product(
                    lookId = lookId,
                    index = index
                )
            }
            return """{"data":{"lookInfo":${lookInfo(lookId)},"products":[$products]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        }

        fun basketResponse(): String {
            val lines = (1..2).joinToString(",") { index ->
                val lookId = DEFAULT_MOCK_LOOK_ID + index - 1
                basketLine(
                    lookId = lookId,
                    index = index
                )
            }
            val looks = (1..2).joinToString(",") { index ->
                val lookId = DEFAULT_MOCK_LOOK_ID + index - 1
                """{"imageUrl":"https://picsum.photos/seed/vp-client-look-$lookId/900/1200","lookId":"$lookId","name":"${lookName(lookId)}"}"""
            }
            return """{"data":{"editor":"mock","id":"mock-basket","lines":[$lines],"looks":[$looks],"catalogActionDisclaimer":null,"timestamp":"2026-07-07T10:00:00Z","version":1},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":null}"""
        }

        fun compilationInfo(compilationId: Int): String {
            return """{"id":$compilationId,"collageUrl":"https://picsum.photos/seed/vp-client-compilation-$compilationId/900/1200","photoUrl":"https://picsum.photos/seed/vp-client-compilation-$compilationId/900/1200","name":"${compilationName(compilationId)}","description":"${compilationDescription(compilationId)}","createDate":"2026-07-07T10:00:00Z","looksQty":5,"lookProductsQty":30,"isStatsAvailable":false}"""
        }

        fun lookInfo(lookId: Int): String {
            return """{"id":$lookId,"collageUrl":"https://picsum.photos/seed/vp-client-look-$lookId/900/1200","photoUrl":"https://picsum.photos/seed/vp-client-look-$lookId/900/1200","meta":null,"name":"${lookName(lookId)}","createDate":"2026-07-07T10:00:00Z","lookProductsQty":6,"isStatsAvailable":false}"""
        }

        fun basketLine(lookId: Int, index: Int): String {
            return """{"lineId":"mock-basket-line-$index","lookId":"$lookId","order":$index,"paySwitch":true,"products":[{"productId":"mock-product-$lookId-$index","product":${product(lookId, index)}}],"quantity":1,"barcode":null,"locationId":null,"locationAsString":null,"controls":null,"alternatives":[]}"""
        }

        fun product(lookId: Int, index: Int): String {
            val imageUrl = "https://picsum.photos/seed/vp-client-product-$lookId-$index/800/1100"
            return """{"oneSize":true,"article":"MOCK-$lookId-$index","brand":"${productBrand(index)}","urlBrandLogo":null,"colorId":"${productColorId(index)}","colorName":"${productColorName(index)}","eKttId":"mock-ektt-$lookId-$index","id":"mock-product-$lookId-$index","imageUrl":"$imageUrl","imageUrls":["$imageUrl"],"isCharity":false,"isSeasonDisplay":true,"itemId":"mock-item-$lookId-$index","lookId":"$lookId","name":"${productName(index)}","order":$index,"paySwitch":true,"price":${productPrice(index)},"priceWithoutDiscount":${productPriceWithoutDiscount(index)},"currentRetailPrice":${productPrice(index)},"quantity":1,"season":"FW26","sizes":[{"availableStockQuantity":2.0,"id":"NS","inOrder":false,"inStock":true,"inStockShops":["BLV"],"isFavorite":false,"isLastInStock":false,"name":"One Size","sizeForFilter":"NS","onlyInVipSite":false,"onlyInTransit":false}],"actions":[],"onlyInTransit":false,"onlyInVipSite":false,"breadcrumbs":["Женское","Подборка"],"compilationLookProductId":$lookId$index,"isGiftCard":false,"discountPercentage":0,"additionalColors":[]}"""
        }

        fun compilationName(compilationId: Int): String {
            return when (compilationId) {
                101 -> "Новые поступления"
                102 -> "Sporty&Riche"
                103 -> "BLV/Hotel"
                104 -> "Летние образы"
                105 -> "Деловая поездка"
                106 -> "Вечерний выход"
                else -> "Mock подборка"
            }
        }

        fun compilationDescription(compilationId: Int): String {
            return when (compilationId) {
                101 -> "Свежие образы недели"
                102 -> "Комфортные комплекты для города"
                103 -> "Капсула для путешествия"
                104 -> "Легкие комплекты для жарких дней"
                105 -> "Вещи для встреч и ужинов"
                106 -> "Акцентные образы для мероприятий"
                else -> "Подборка консультанта"
            }
        }

        fun lookName(lookId: Int): String {
            val index = lookId % 100
            return when (index) {
                1 -> "ОБРАЗ С ЖАКЕТОМ И ШЕЛКОВОЙ ЮБКОЙ"
                2 -> "ОБРАЗ С ТРЕНЧЕМ И КАШЕМИРОМ"
                3 -> "ОБРАЗ С ПЛАТЬЕМ И МЮЛЯМИ"
                4 -> "ОБРАЗ С БРЮКАМИ И ТОПОМ"
                else -> "ОБРАЗ С АКЦЕНТНЫМ АКСЕССУАРОМ"
            }
        }

        fun productBrand(index: Int): String {
            return when (index) {
                1 -> "SAINT LAURENT"
                2 -> "BRUNELLO CUCINELLI"
                3 -> "LORO PIANA"
                4 -> "THE ROW"
                5 -> "JIL SANDER"
                else -> "BOTTEGA VENETA"
            }
        }

        fun productName(index: Int): String {
            return when (index) {
                1 -> "Жакет из шерсти"
                2 -> "Юбка из шелка"
                3 -> "Джемпер из кашемира"
                4 -> "Брюки прямого кроя"
                5 -> "Кожаные мюли"
                else -> "Сумка с плетением"
            }
        }

        fun productColorId(index: Int): String {
            return when (index) {
                1 -> "black"
                2 -> "ivory"
                3 -> "grey"
                4 -> "navy"
                5 -> "beige"
                else -> "green"
            }
        }

        fun productColorName(index: Int): String {
            return when (index) {
                1 -> "Черный"
                2 -> "Айвори"
                3 -> "Серый"
                4 -> "Темно-синий"
                5 -> "Бежевый"
                else -> "Зеленый"
            }
        }

        fun productPrice(index: Int): Double {
            return when (index) {
                1 -> 219_900.0
                2 -> 129_900.0
                3 -> 92_500.0
                4 -> 74_900.0
                5 -> 68_500.0
                else -> 189_900.0
            }
        }

        fun productPriceWithoutDiscount(index: Int): String {
            return when (index) {
                2 -> "159900.0"
                5 -> "82500.0"
                else -> "null"
            }
        }

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
