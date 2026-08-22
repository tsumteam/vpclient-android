package ru.mercury.vpclient.features.loyalty_add_card_sheet.model

import ru.mercury.vpclient.shared.data.entity.LoyaltyAddCardMode
import ru.mercury.vpclient.shared.mvi.Model

data class LoyaltyAddCardModel(
    val mode: LoyaltyAddCardMode = LoyaltyAddCardMode.Phone,
    val phone: String = "",
    val cardNumber: String = "",
    val isLoading: Boolean = false,
    val isPhoneErrorVisible: Boolean = false
): Model {

    val isConfirmEnabled: Boolean
        get() = when (mode) {
            LoyaltyAddCardMode.Phone -> phone.isNotBlank()
            LoyaltyAddCardMode.CardNumber -> cardNumber.isNotBlank()
        }
}
