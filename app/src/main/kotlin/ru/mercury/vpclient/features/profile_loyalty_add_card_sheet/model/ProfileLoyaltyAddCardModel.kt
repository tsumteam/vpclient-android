package ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.model

import ru.mercury.vpclient.shared.mvi.Model

data class ProfileLoyaltyAddCardModel(
    val mode: ProfileLoyaltyAddCardMode = ProfileLoyaltyAddCardMode.Phone,
    val phone: String = "",
    val cardNumber: String = "",
    val isLoading: Boolean = false,
    val isPhoneErrorVisible: Boolean = false,
    val isCardErrorVisible: Boolean = false
): Model {

    val isConfirmEnabled: Boolean
        get() {
            return when (mode) {
                ProfileLoyaltyAddCardMode.Phone -> phone.isNotBlank()
                ProfileLoyaltyAddCardMode.CardNumber -> cardNumber.isNotBlank()
            }
        }
}

enum class ProfileLoyaltyAddCardMode {
    Phone,
    CardNumber
}
