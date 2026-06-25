package ru.mercury.vpclient.features.profile_order.model

import ru.mercury.vpclient.shared.data.event.ProfileOrderDeliveryGroupState
import ru.mercury.vpclient.shared.mvi.Model

data class ProfileOrderModel(
    val orderNumber: String = "",
    val amount: String = "",
    val creationDate: String = "",
    val isLoading: Boolean = true,
    val isPaymentAlertVisible: Boolean = false,
    val paymentAlertRemainingMinutes: Int = 0,
    val deliveryGroups: List<ProfileOrderDeliveryGroupState> = emptyList()
): Model
