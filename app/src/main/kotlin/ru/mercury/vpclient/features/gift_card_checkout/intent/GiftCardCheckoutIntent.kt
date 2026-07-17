package ru.mercury.vpclient.features.gift_card_checkout.intent

import ru.mercury.vpclient.features.gift_card_checkout.navigation.GiftCardCheckoutRoute
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface GiftCardCheckoutIntent: Intent {
    data object BackClick: GiftCardCheckoutIntent
    data object CheckPaymentResult: GiftCardCheckoutIntent
    data object PayByCardClick: GiftCardCheckoutIntent
    data object PayBySbpClick: GiftCardCheckoutIntent
    data class LoadData(val route: GiftCardCheckoutRoute): GiftCardCheckoutIntent
    data class EmailChange(val value: String): GiftCardCheckoutIntent
    data class PhoneChange(val value: String): GiftCardCheckoutIntent
    data class SelectDay(val dayId: String): GiftCardCheckoutIntent
    data class SelectInterval(val intervalId: String): GiftCardCheckoutIntent
}
