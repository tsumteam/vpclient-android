package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.FORMAT_RUB
import ru.mercury.vpclient.shared.data.entity.ProfileOrder
import ru.mercury.vpclient.shared.data.entity.ProfileOrderDelivery
import ru.mercury.vpclient.shared.data.entity.ProfileOrderDetails
import ru.mercury.vpclient.shared.data.entity.ProfileOrderDetailsProduct
import ru.mercury.vpclient.shared.data.network.response.DeliveryTimeResponse
import ru.mercury.vpclient.shared.data.network.response.OrderDeliveryResponse
import ru.mercury.vpclient.shared.data.network.type.OrderPaymentStatus
import ru.mercury.vpclient.shared.data.network.response.OrderProductResponse
import ru.mercury.vpclient.shared.data.network.response.OrderResponse
import ru.mercury.vpclient.shared.data.network.response.OrderResponseWithBadgeDtoResponse
import ru.mercury.vpclient.shared.data.network.response.ProfileOrdersSaleItemResponse
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun ProfileOrdersSaleItemResponse.toProfileOrder(): ProfileOrder? {
    val sale = sale ?: return null
    val orderNumber = sale.orderNumber?.trim().orEmpty()
    if (orderNumber.isEmpty()) return null

    val isFinished = sale.isFinished == true
    val paymentStatus = sale.paymentStatusAsString
        ?.trim()
        ?.lowercase(Locale.forLanguageTag("ru-RU"))
        .orEmpty()
    val deliveryStatus = sale.isDeliveredAsString
        ?.trim()
        ?.lowercase(Locale.forLanguageTag("ru-RU"))
        ?.takeIf { it.isNotEmpty() }
        ?: when {
            sale.isDelivered == true -> "доставлен"
            else -> "не доставлен"
        }

    return ProfileOrder(
        id = sale.id ?: orderNumber.hashCode(),
        orderNumber = orderNumber,
        amount = sale.totalPrice.formattedProfileOrderPrice,
        isFinished = isFinished,
        statusPrefix = sale.isFinishedAsString
            ?.trim()
            ?.let { value ->
                when {
                    value.endsWith(":") -> value
                    else -> "$value:"
                }
            }
            .orEmpty(),
        statusDescription = listOf(paymentStatus, deliveryStatus)
            .filter { it.isNotEmpty() }
            .joinToString(separator = ", "),
        imageUrls = sale.imageUrls.orEmpty().filter { it.isNotBlank() },
        productsCount = sale.productsQty ?: sale.imageUrls.orEmpty().size,
        showPaymentBadge = sale.isOnlinePayAvailable == true,
        isReceipt = sale.salesType == "crm"
    )
}

fun OrderResponseWithBadgeDtoResponse.toProfileOrderDetails(): ProfileOrderDetails {
    return order?.toProfileOrderDetails() ?: ProfileOrderDetails(
        orderNumber = "",
        amount = "",
        creationDate = "",
        isPaymentAlertVisible = false,
        paymentAlertRemainingMinutes = 0,
        deliveries = emptyList()
    )
}

fun OrderResponse.toProfileOrderDetails(): ProfileOrderDetails {
    val paymentAlertRemainingMinutes = creationDate.profileOrderPaymentAlertRemainingMinutes

    return ProfileOrderDetails(
        orderNumber = orderNumber?.trim().orEmpty(),
        amount = when {
            totalPrice != null -> totalPrice.formattedProfileOrderPrice
            else -> ""
        },
        creationDate = creationDate.profileOrderCreationDate,
        isPaymentAlertVisible = controls?.isOnlinePayAvailable == true && paymentAlertRemainingMinutes > 0,
        paymentAlertRemainingMinutes = paymentAlertRemainingMinutes,
        deliveries = deliveries.orEmpty().mapIndexed { index, delivery ->
            delivery.toProfileOrderDelivery(
                index = index,
                isOrderWithGiftCard = isOrderWithGiftCard == true,
                paymentStatus = paymentStatus,
                paymentStatusAsString = paymentStatusAsString.orEmpty()
            )
        }
    )
}

private fun OrderDeliveryResponse.toProfileOrderDelivery(
    index: Int,
    isOrderWithGiftCard: Boolean,
    paymentStatus: OrderPaymentStatus?,
    paymentStatusAsString: String
): ProfileOrderDelivery {
    val giftCardProduct = when {
        isOrderWithGiftCard -> products.orEmpty().firstOrNull()
        else -> products.orEmpty().firstOrNull { product -> product.isOrderVirtualGiftCard == true }
    }
    val isGiftCardDelivery = isOrderWithGiftCard || giftCardProduct?.isOrderVirtualGiftCard == true

    return ProfileOrderDelivery(
        id = deliveryId.orEmpty().ifBlank { "delivery_$index" },
        date = when {
            isGiftCardDelivery -> giftCardProduct?.giftCardSendDateTime.profileOrderGiftCardSendDate
            else -> deliveryTime.profileOrderDeliveryDate
        },
        address = when {
            isGiftCardDelivery -> listOf(
                giftCardProduct?.giftCardEmailReceiver.orEmpty(),
                formatPhoneForDisplay(giftCardProduct?.giftCardPhoneReceiver.orEmpty())
            ).filter { value -> value.isNotBlank() }.joinToString(separator = ", ")
            else -> address?.address.orEmpty()
        },
        products = products.orEmpty().mapIndexed { productIndex, product ->
            product.toProfileOrderDetailsProduct(
                index = productIndex,
                paymentStatus = paymentStatus,
                paymentStatusAsString = paymentStatusAsString
            )
        }
    )
}

private fun OrderProductResponse.toProfileOrderDetailsProduct(
    index: Int,
    paymentStatus: OrderPaymentStatus?,
    paymentStatusAsString: String
): ProfileOrderDetailsProduct {
    val product = product
    val imageUrls = product?.imageUrls.orEmpty().filter { it.isNotBlank() }
    val rawImageUrl = product?.imageUrl.orEmpty()
    val priceValue = price ?: product?.price ?: product?.currentRetailPrice

    return ProfileOrderDetailsProduct(
        id = lineId.orEmpty().ifBlank { product?.id ?: product?.itemId ?: "product_$index" },
        productId = product?.id ?: product?.itemId.orEmpty(),
        imageUrl = rawImageUrl.ifBlank { imageUrls.firstOrNull().orEmpty() },
        brand = product?.brand.orEmpty(),
        urlBrandLogo = product?.urlBrandLogo,
        name = product?.name.orEmpty(),
        color = product?.colorName.orEmpty(),
        article = product?.article?.takeIf { it.isNotBlank() } ?: product?.itemId.orEmpty(),
        price = when {
            priceValue != null -> priceValue.formattedProfileOrderPrice
            else -> ""
        },
        size = product?.sizes.orEmpty()
            .mapNotNull { size -> size.name?.takeIf { it.isNotBlank() } }
            .joinToString(separator = " | "),
        status = when (paymentStatus) {
            OrderPaymentStatus.NOT_PAID -> paymentStatusAsString
            else -> logisticStatusAsStringForClient.orEmpty()
        },
        quantity = product?.quantity?.takeIf { it > 0 } ?: 1,
        isGiftCard = isOrderVirtualGiftCard == true
    )
}

private val String?.profileOrderCreationDate: String
    get() {
        val value = this?.trim().orEmpty()
        val date = value.profileOrderLocalDate() ?: return ""
        return date.format(profileOrderCreationDateFormatter)
    }

private val DeliveryTimeResponse?.profileOrderDeliveryDate: String
    get() {
        val fromDateTime = this?.fromValue?.profileOrderLocalDateTime()
        val toDateTime = this?.to?.profileOrderLocalDateTime()
        val fromTime = fromDateTime?.format(profileOrderTimeFormatter).orEmpty()
        val toTime = toDateTime?.format(profileOrderTimeFormatter).orEmpty()

        return when {
            fromDateTime != null && fromTime.isNotEmpty() && toTime.isNotEmpty() -> {
                "${fromDateTime.format(profileOrderDeliveryDateFormatter)} с $fromTime до $toTime"
            }
            fromDateTime != null -> fromDateTime.format(profileOrderDeliveryDateFormatter)
            else -> listOf(this?.fromValue, this?.to).filterNotNull().joinToString(" - ")
        }
    }

private val String?.profileOrderGiftCardSendDate: String
    get() {
        val dateTime = this?.profileOrderLocalDateTime() ?: return ""
        return dateTime.format(profileOrderGiftCardSendDateFormatter)
    }

private fun String.profileOrderLocalDateTime(): LocalDateTime? {
    return runCatching { OffsetDateTime.parse(this).toLocalDateTime() }
        .getOrNull()
        ?: runCatching { OffsetDateTime.parse(this, profileOrderApiDateTimeFormatter).toLocalDateTime() }.getOrNull()
        ?: runCatching { LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }.getOrNull()
}

private val String?.profileOrderPaymentAlertRemainingMinutes: Int
    get() {
        val createdAt = this?.trim().orEmpty().profileOrderInstant() ?: return 0
        val elapsedMinutes = Duration.between(createdAt, Instant.now()).toMinutes()
        return (PROFILE_ORDER_PAYMENT_CANCELLATION_TIMEOUT_MINUTES - elapsedMinutes)
            .coerceAtLeast(0)
            .toInt()
    }

private fun String.profileOrderInstant(): Instant? {
    return runCatching { OffsetDateTime.parse(this).toInstant() }
        .getOrNull()
        ?: runCatching {
            LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        }.getOrNull()
}

private fun String.profileOrderLocalDate(): LocalDate? {
    return profileOrderLocalDateTime()?.toLocalDate()
        ?: runCatching { LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE) }.getOrNull()
}

private val profileOrderPriceFormatter = DecimalFormat(
    "#,###",
    DecimalFormatSymbols(Locale.Builder().setLanguage("ru").setRegion("RU").build()).apply {
        groupingSeparator = ' '
    }
)

private val Double?.formattedProfileOrderPrice: String
    get() = FORMAT_RUB.format(profileOrderPriceFormatter.format(this.orEmpty))

private val profileOrderLocale = Locale.forLanguageTag("ru")

private val profileOrderCreationDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", profileOrderLocale)

private val profileOrderApiDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)

private val profileOrderDeliveryDateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", profileOrderLocale)

private val profileOrderGiftCardSendDateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", profileOrderLocale)

private val profileOrderTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", profileOrderLocale)

private const val PROFILE_ORDER_PAYMENT_CANCELLATION_TIMEOUT_MINUTES = 180L
