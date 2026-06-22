package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationData
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.network.entity.CheckOutAddressesDto
import ru.mercury.vpclient.shared.data.network.entity.DeliveryTimeDto
import ru.mercury.vpclient.shared.data.network.entity.FittingDeliveryTimeProductDto
import ru.mercury.vpclient.shared.data.network.entity.FittingDeliveryTimeResponseDto
import ru.mercury.vpclient.shared.data.network.entity.GetDeliveryIntervalsForExistingFittingResponseDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val fittingLocale = Locale.forLanguageTag("ru")
private val fittingDateFormatter = DateTimeFormatter.ofPattern("d MMMM", fittingLocale)
private val fittingTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", fittingLocale)

fun FittingDeliveryTimeResponseDto.fittingConfirmationData(
    addresses: CheckOutAddressesDto,
    selectedProducts: List<CartProduct>
): FittingConfirmationData {
    return FittingConfirmationData(
        boutiqueAddress = addresses.boutiqueAddress?.address,
        clientAddress = addresses.clientAddress?.address,
        isClientAddressAvailable = addresses.controls?.isDeliveryToClientAvailable ?: true,
        singleIntervals = deliveryTimes.orEmpty().map { it.fittingConfirmationDeliveryInterval },
        deliveryGroups = deliveries.orEmpty().mapIndexed { index, delivery ->
            val deliveryProducts = delivery.products.orEmpty().cartProducts(selectedProducts)
            FittingConfirmationDeliveryGroup(
                id = "delivery_$index",
                products = deliveryProducts.ifEmpty { selectedProducts },
                intervals = delivery.deliveryTimes.orEmpty().map { it.fittingConfirmationDeliveryInterval }
            )
        }
    )
}

fun GetDeliveryIntervalsForExistingFittingResponseDto.fittingConfirmationData(
    addresses: CheckOutAddressesDto,
    deliveryId: String,
    selectedProducts: List<CartProduct>
): FittingConfirmationData {
    val intervals = deliveryTimes.orEmpty().map { it.fittingConfirmationDeliveryInterval }

    return FittingConfirmationData(
        boutiqueAddress = addresses.boutiqueAddress?.address,
        clientAddress = addresses.clientAddress?.address,
        isClientAddressAvailable = addresses.controls?.isDeliveryToClientAvailable ?: true,
        singleIntervals = intervals,
        deliveryGroups = listOf(
            FittingConfirmationDeliveryGroup(
                id = deliveryId.ifBlank { "delivery_0" },
                products = selectedProducts,
                intervals = intervals
            )
        )
    )
}

val DeliveryTimeDto.fittingConfirmationDeliveryInterval: FittingConfirmationDeliveryInterval
    get() {
        val fromDateTime = fromValue?.fittingLocalDateTime()
        val toDateTime = to?.fittingLocalDateTime()
        val dayId = fromDateTime?.toLocalDate()?.toString() ?: fromValue.orEmpty()
        val dayTitle = fromDateTime?.toLocalDate()?.fittingDayTitle() ?: fromValue.orEmpty()
        val fromTime = fromDateTime?.format(fittingTimeFormatter).orEmpty()
        val toTime = toDateTime?.format(fittingTimeFormatter).orEmpty()
        val timeTitle = when {
            fromTime.isNotEmpty() && toTime.isNotEmpty() -> "$fromTime-$toTime"
            else -> listOf(fromValue, to).filterNotNull().joinToString("-")
        }
        val summary = when {
            fromDateTime != null && fromTime.isNotEmpty() && toTime.isNotEmpty() -> {
                "${fromDateTime.format(fittingDateFormatter)} c $fromTime до $toTime"
            }
            else -> timeTitle
        }

        return FittingConfirmationDeliveryInterval(
            id = "${fromValue.orEmpty()}_${to.orEmpty()}",
            dayId = dayId,
            dayTitle = dayTitle,
            timeTitle = timeTitle,
            summary = summary,
            from = fromValue,
            to = to
        )
    }

val FittingConfirmationDeliveryInterval.deliveryTimeDto: DeliveryTimeDto
    get() = DeliveryTimeDto(
        fromValue = from,
        to = to
    )

private fun List<FittingDeliveryTimeProductDto>.cartProducts(
    selectedProducts: List<CartProduct>
): List<CartProduct> {
    val ids = mapNotNull { product ->
        product.productId
            ?: product.product?.id
            ?: product.product?.itemId
    }.toSet()

    return selectedProducts.filter { product ->
        product.id in ids || product.detailId in ids || product.itemId in ids
    }
}

private fun String.fittingLocalDateTime(): LocalDateTime? {
    return runCatching { OffsetDateTime.parse(this).toLocalDateTime() }
        .getOrNull()
        ?: runCatching { LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }.getOrNull()
}

private fun LocalDate.fittingDayTitle(): String {
    val today = LocalDate.now()
    return when (this) {
        today -> "Сегодня"
        today.plusDays(1) -> "Завтра"
        else -> format(fittingDateFormatter)
    }
}
