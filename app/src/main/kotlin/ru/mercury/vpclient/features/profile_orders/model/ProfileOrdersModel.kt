package ru.mercury.vpclient.features.profile_orders.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.entity.ProfileOrder
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderItemState
import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderProductState
import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderStatusType
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

data class ProfileOrdersModel(
    val isRefreshing: Boolean = false,
    val cartCount: Int = 0,
    val cartBadge: Int = 0,
    val fittingCount: Int = 0,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty
): Model {

    val cartText: String
        get() = when {
            cartCount > 0 -> cartCount.toString()
            else -> ""
        }

    val isCartBadgeVisible: Boolean
        get() = cartBadge > 0

    val fittingText: String
        get() = if (fittingCount > 0) fittingCount.toString() else ""

    val isFittingButtonVisible: Boolean
        get() = fittingCount > 0

    val isFittingBadgeVisible: Boolean
        get() = activeEmployee.hasFittingBadge

    val isMessengerBadgeVisible: Boolean
        get() = activeEmployee.hasMessengerBadge
}

fun ProfileOrder.toProfileOrderItemState(): ProfileOrderItemState {
    val visibleImages = when {
        imageUrls.size <= 4 -> imageUrls
        else -> imageUrls.take(4)
    }

    return ProfileOrderItemState(
        numberTitleRes = when {
            isReceipt -> ClientStrings.ProfileOrdersReceiptNumber
            else -> ClientStrings.ProfileOrdersNumber
        },
        orderNumber = orderNumber,
        amount = amount,
        statusPrefix = statusPrefix,
        statusDescription = statusDescription,
        statusType = when {
            isFinished -> ProfileOrderStatusType.Finished
            else -> ProfileOrderStatusType.NotFinished
        },
        showPaymentBadge = showPaymentBadge,
        products = visibleImages.map { imageUrl ->
            ProfileOrderProductState(
                imageUrl = imageUrl
            )
        },
        hiddenProductsCount = (productsCount - visibleImages.size).coerceAtLeast(0)
    )
}

internal fun profileOrdersPreviewItems(): List<ProfileOrderItemState> = listOf(
    ProfileOrderItemState(
        numberTitleRes = ClientStrings.ProfileOrdersNumber,
        orderNumber = "4143-31Т",
        amount = "1 359 950 ₽",
        statusPrefix = "Не завершен:",
        statusDescription = "в процессе оплаты, не доставлен",
        statusType = ProfileOrderStatusType.NotFinished,
        showPaymentBadge = true,
        products = List(4) {
            ProfileOrderProductState(
                imageUrl = ""
            )
        },
        hiddenProductsCount = 2
    ),
    ProfileOrderItemState(
        numberTitleRes = ClientStrings.ProfileOrdersNumber,
        orderNumber = "72878-Т",
        amount = "200 000 ₽",
        statusPrefix = "Не завершен:",
        statusDescription = "не оплачен, не доставлен",
        statusType = ProfileOrderStatusType.NotFinished,
        showPaymentBadge = false,
        products = List(2) {
            ProfileOrderProductState(
                imageUrl = ""
            )
        }
    ),
    ProfileOrderItemState(
        numberTitleRes = ClientStrings.ProfileOrdersNumber,
        orderNumber = "728778-Т",
        amount = "459 000 ₽",
        statusPrefix = "Завершен:",
        statusDescription = "оплачен, доставлен",
        statusType = ProfileOrderStatusType.Finished,
        showPaymentBadge = false,
        products = List(2) {
            ProfileOrderProductState(
                imageUrl = ""
            )
        }
    )
)
