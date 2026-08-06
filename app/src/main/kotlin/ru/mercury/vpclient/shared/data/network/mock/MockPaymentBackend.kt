package ru.mercury.vpclient.shared.data.network.mock

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Request
import okio.Buffer
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class MockPaymentBackend {

    private val orderSequence = AtomicInteger(DEFAULT_ORDER_SEQUENCE)
    private val orderStatuses = ConcurrentHashMap<String, String>()

    fun responseBody(
        request: Request,
        loyaltyCardNumber: String?
    ): String? {
        val endpoint = request.mockPaymentEndpoint
        return when {
            endpoint.isFittingCheckoutEndpoint -> fittingCheckoutResponse(
                loyaltyCardNumber = loyaltyCardNumber,
                useBonuses = request.bonusType == LOYALTY_CARD_BONUS_TYPE
            )
            endpoint in ORDER_CREATION_ENDPOINTS -> createOrderResponse()
            endpoint.isReserveBonusesEndpoint -> orderOperationResponse(
                orderId = endpoint.orderId,
                paymentStatus = BONUS_RESERVATION_STARTED_STATUS
            )
            endpoint.isConfirmBonusesEndpoint -> orderOperationResponse(
                orderId = endpoint.orderId,
                paymentStatus = BONUS_RESERVATION_FINISHED_STATUS
            )
            endpoint.isPaymentLinkEndpoint -> paymentLinkResponse(endpoint.orderId)
            endpoint.isSbpPaymentEndpoint -> sbpPaymentResponse(endpoint.orderId)
            endpoint.isCloudPaymentConfirmEndpoint -> orderOperationResponse(
                orderId = endpoint.orderId,
                paymentStatus = PAYMENT_FINISHED_STATUS
            )
            endpoint.isCloudPaymentEndpoint -> orderOperationResponse(
                orderId = endpoint.orderId,
                paymentStatus = PAYMENT_STARTED_STATUS
            )
            endpoint.isOrderEndpoint && request.method == GET_METHOD -> orderResponse(endpoint.orderId)
            endpoint == PAYMENT_RECONCILIATION_ENDPOINT -> PAYMENT_RECONCILIATION_RESPONSE
            endpoint == PAYMENT_ACKNOWLEDGE_ENDPOINT -> EMPTY_OPERATION_RESPONSE
            endpoint in PAYMENT_SCHEDULER_ENDPOINTS -> STRING_OPERATION_RESPONSE
            else -> null
        }
    }

    private fun createOrderResponse(): String {
        val orderId = "$MOCK_ORDER_PREFIX${orderSequence.incrementAndGet()}"
        orderStatuses[orderId] = NOT_PAID_STATUS
        return baseResponse(
            data = """{"id":"$orderId","orderNumber":"$orderId","creationDate":"$ORDER_CREATED_AT","paymentStatus":"$NOT_PAID_STATUS","totalPrice":$ORDER_TOTAL_PRICE,"isFinished":false,"isDelivered":false}"""
        )
    }

    private fun orderOperationResponse(
        orderId: String,
        paymentStatus: String
    ): String {
        orderStatuses[orderId] = paymentStatus
        return EMPTY_OPERATION_RESPONSE
    }

    private fun paymentLinkResponse(orderId: String): String {
        orderStatuses[orderId] = PAYMENT_FINISHED_STATUS
        return baseResponse(
            data = """{"urlPayment":"$MOCK_PAYMENT_URL?orderId=$orderId"}"""
        )
    }

    private fun sbpPaymentResponse(orderId: String): String {
        orderStatuses[orderId] = PAYMENT_FINISHED_STATUS
        return baseResponse(
            data = """{"qrCodeUrl":"$MOCK_SBP_URL?orderId=$orderId"}"""
        )
    }

    private fun orderResponse(orderId: String): String {
        val paymentStatus = orderStatuses[orderId] ?: NOT_PAID_STATUS
        return baseResponse(
            data = """{"badge":1,"order":{"id":"$orderId","orderNumber":"$orderId","creationDate":"$ORDER_CREATED_AT","paymentStatus":"$paymentStatus","paymentStatusAsString":"$paymentStatus","totalPrice":$ORDER_TOTAL_PRICE,"isFinished":${paymentStatus == PAYMENT_FINISHED_STATUS},"isDelivered":false,"deliveries":[{"deliveryId":"$MOCK_DELIVERY_ID","deliveryTime":{"from":"$DELIVERY_FROM","to":"$DELIVERY_TO"},"address":{"address":"$DELIVERY_ADDRESS","comment":""},"products":[]}]}}"""
        )
    }

    private fun fittingCheckoutResponse(
        loyaltyCardNumber: String?,
        useBonuses: Boolean
    ): String {
        val linkedCardNumber = loyaltyCardNumber.orEmpty()
        val totalAvailableBonuses = when {
            linkedCardNumber.isBlank() -> 0
            else -> TOTAL_AVAILABLE_BONUSES
        }
        val availableBonusSum = when {
            linkedCardNumber.isNotBlank() && useBonuses -> AVAILABLE_BONUS_SUM
            else -> 0
        }
        return baseResponse(
            data = """{"fittingResponseDto":{"fittingNumber":"$MOCK_FITTING_ID","id":"$MOCK_FITTING_ID","deliveries":[{"deliveryId":"$MOCK_DELIVERY_ID","address":"$DELIVERY_ADDRESS","deliveryTime":{"from":"$DELIVERY_FROM","to":"$DELIVERY_TO"},"lines":[${fittingLine(index = 1, price = 129900, priceWithoutDiscount = 159900)},${fittingLine(index = 2, price = 78900, priceWithoutDiscount = 78900)}]}]},"availableBonusSum":$availableBonusSum,"loyaltyCardNumber":"$linkedCardNumber","totalAvailableBonuses":$totalAvailableBonuses,"isGiftCertificateUsageAllowed":false}"""
        )
    }

    private fun fittingLine(
        index: Int,
        price: Int,
        priceWithoutDiscount: Int
    ): String {
        return """{"lineId":"mock-payment-line-$index","order":$index,"paySwitch":true,"product":{"id":"mock-payment-product-$index","itemId":"mock-payment-item-$index","name":"${productName(index)}","brand":"${productBrand(index)}","price":$price,"priceWithoutDiscount":$priceWithoutDiscount,"currentRetailPrice":$price,"quantity":1,"paySwitch":true,"oneSize":true,"isGiftCard":false}}"""
    }

    private fun productName(index: Int): String {
        return when (index) {
            1 -> "Жакет из шерсти"
            else -> "Брюки прямого кроя"
        }
    }

    private fun productBrand(index: Int): String {
        return when (index) {
            1 -> "SAINT LAURENT"
            else -> "LORO PIANA"
        }
    }

    private fun baseResponse(data: String): String {
        return """{"data":$data,"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-payment"}"""
    }

    private val Request.mockPaymentEndpoint: String
        get() = url.encodedPath
            .trim('/')
            .removePrefix("$API_PATH_PREFIX/")

    private val Request.bodyText: String
        get() {
            val buffer = Buffer()
            body?.writeTo(buffer)
            return buffer.readUtf8()
        }

    private val Request.bonusType: String?
        get() {
            val bodyObject = runCatching {
                Json.parseToJsonElement(bodyText).jsonObject
            }.getOrNull() ?: return null
            return bodyObject["bonusType"]?.jsonPrimitive?.contentOrNull
        }

    private val String.isFittingCheckoutEndpoint: Boolean
        get() = startsWith(FITTINGS_ENDPOINT_PREFIX) && endsWith(FITTINGS_CHECKOUT_ENDPOINT_SUFFIX)

    private val String.isOrderEndpoint: Boolean
        get() = startsWith(ORDERS_ENDPOINT_PREFIX) && count { char -> char == '/' } == 1

    private val String.isReserveBonusesEndpoint: Boolean
        get() = startsWith(ORDERS_ENDPOINT_PREFIX) && endsWith(RESERVE_BONUSES_ENDPOINT_SUFFIX)

    private val String.isConfirmBonusesEndpoint: Boolean
        get() = startsWith(ORDERS_ENDPOINT_PREFIX) && endsWith(CONFIRM_BONUSES_ENDPOINT_SUFFIX)

    private val String.isPaymentLinkEndpoint: Boolean
        get() = startsWith(ORDERS_ENDPOINT_PREFIX) && endsWith(PAYMENT_LINK_ENDPOINT_SUFFIX)

    private val String.isSbpPaymentEndpoint: Boolean
        get() = startsWith(ORDERS_ENDPOINT_PREFIX) && endsWith(SBP_PAYMENT_ENDPOINT_SUFFIX)

    private val String.isCloudPaymentEndpoint: Boolean
        get() = startsWith(ORDERS_ENDPOINT_PREFIX) && endsWith(CLOUD_PAYMENT_ENDPOINT_SUFFIX)

    private val String.isCloudPaymentConfirmEndpoint: Boolean
        get() = startsWith(ORDERS_ENDPOINT_PREFIX) && endsWith(CLOUD_PAYMENT_CONFIRM_ENDPOINT_SUFFIX)

    private val String.orderId: String
        get() = removePrefix(ORDERS_ENDPOINT_PREFIX).substringBefore('/')

    private companion object {
        private const val API_PATH_PREFIX = "api"
        private const val GET_METHOD = "GET"
        private const val FITTINGS_ENDPOINT_PREFIX = "fittings/"
        private const val FITTINGS_CHECKOUT_ENDPOINT_SUFFIX = "/for-checkout"
        private const val ORDERS_ENDPOINT_PREFIX = "orders/"
        private const val RESERVE_BONUSES_ENDPOINT_SUFFIX = "/reserve-bonuses"
        private const val CONFIRM_BONUSES_ENDPOINT_SUFFIX = "/confirm-bonuses"
        private const val PAYMENT_LINK_ENDPOINT_SUFFIX = "/payment-link"
        private const val SBP_PAYMENT_ENDPOINT_SUFFIX = "/payment/sbp"
        private const val CLOUD_PAYMENT_ENDPOINT_SUFFIX = "/payment/cloud-payment"
        private const val CLOUD_PAYMENT_CONFIRM_ENDPOINT_SUFFIX = "/payment/cloud-payment/confirm"
        private const val PAYMENT_RECONCILIATION_ENDPOINT = "axapta/payments-reconciliation"
        private const val PAYMENT_ACKNOWLEDGE_ENDPOINT = "axapta/loyalty/basketPaymAcknowledge"
        private const val LOYALTY_CARD_BONUS_TYPE = "loyaltyCard"
        private const val NOT_PAID_STATUS = "notPaid"
        private const val BONUS_RESERVATION_STARTED_STATUS = "bonusReservationStarted"
        private const val BONUS_RESERVATION_FINISHED_STATUS = "bonusReservationFinished"
        private const val PAYMENT_STARTED_STATUS = "paymentStarted"
        private const val PAYMENT_FINISHED_STATUS = "paymentFinished"
        private const val MOCK_ORDER_PREFIX = "MOCK-ORDER-"
        private const val MOCK_FITTING_ID = "MOCK-FITTING-1"
        private const val MOCK_DELIVERY_ID = "MOCK-DELIVERY-1"
        private const val DEFAULT_ORDER_SEQUENCE = 1000
        private const val ORDER_TOTAL_PRICE = 208800
        private const val TOTAL_AVAILABLE_BONUSES = 12500
        private const val AVAILABLE_BONUS_SUM = 1500
        private const val ORDER_CREATED_AT = "2026-08-06T12:00:00+03:00"
        private const val DELIVERY_FROM = "2026-08-15T14:00:00+03:00"
        private const val DELIVERY_TO = "2026-08-15T16:00:00+03:00"
        private const val DELIVERY_ADDRESS = "Москва, Рублевское ш., д. 9, кв./офис 14"
        private const val MOCK_PAYMENT_URL = "https://example.com/mock-payment-success"
        private const val MOCK_SBP_URL = "https://example.com/mock-sbp-success"

        private const val EMPTY_OPERATION_RESPONSE = """{"data":{},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-payment"}"""
        private const val STRING_OPERATION_RESPONSE = """{"data":"ok","error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-payment"}"""
        private const val PAYMENT_RECONCILIATION_RESPONSE = """{"data":{"items":[]},"error":null,"errors":null,"type":null,"title":null,"status":200,"traceId":"mock-payment"}"""

        private val ORDER_CREATION_ENDPOINTS = setOf(
            "orders/create-from-basket",
            "orders/create-from-fitting",
            "orders/create-with-gift-card"
        )

        private val PAYMENT_SCHEDULER_ENDPOINTS = setOf(
            "internal/scheduler/ordersCheckSbpPaymentStatus",
            "internal/scheduler/ordersForbidPayment"
        )
    }
}
