package ru.mercury.vpclient.features.gift_card_checkout.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.network.type.GiftCardType
import ru.mercury.vpclient.shared.domain.mapper.rubles
import ru.mercury.vpclient.shared.mvi.Model

data class GiftCardCheckoutModel(
    val itemId: String = "",
    val templateId: Int = 0,
    val amount: Int = 0,
    val type: GiftCardType = GiftCardType.NONE,
    val emailText: String = "",
    val phoneText: String = "",
    val deliveryIntervals: List<FittingConfirmationDeliveryInterval> = emptyList(),
    val selectedDayId: String? = null,
    val selectedIntervalId: String? = null,
    val isEmailErrorVisible: Boolean = false,
    val isPhoneErrorVisible: Boolean = false,
    val isPaymentEnabled: Boolean = false,
    val paymentOrderNumber: String = "",
    val loadDataJob: Job? = null,
    val paymentJob: Job? = null,
    val paymentResultCheckJob: Job? = null
): Model {

    val amountText: String
        get() = amount.rubles

    val isLoading: Boolean
        get() = loadDataJob?.isActive == true

    val isPaymentLoading: Boolean
        get() = paymentJob?.isActive == true || paymentResultCheckJob?.isActive == true

    val isPayButtonEnabled: Boolean
        get() = isPaymentEnabled && !isPaymentLoading

    val selectedDayIntervals: List<FittingConfirmationDeliveryInterval>
        get() {
            val dayId = selectedDayId ?: deliveryIntervals.firstOrNull()?.dayId
            return deliveryIntervals.filter { interval -> interval.dayId == dayId }
        }
}
