package ru.mercury.vpclient.features.checkout.intent

import ru.mercury.vpclient.features.checkout_amount_changed_dialog.intent.CheckoutAmountChangedIntent
import ru.mercury.vpclient.features.checkout_bank_card_sheet.intent.CheckoutBankCardIntent
import ru.mercury.vpclient.features.checkout_bonus_sheet.intent.CheckoutBonusIntent
import ru.mercury.vpclient.features.checkout_payment_method_sheet.intent.CheckoutPaymentMethodIntent
import ru.mercury.vpclient.features.checkout_sbp_bank_sheet.intent.CheckoutSbpBankIntent
import ru.mercury.vpclient.features.fitting_addresses.event.FittingAddressesEvent
import ru.mercury.vpclient.features.loyalty_add_card_sheet.intent.LoyaltyAddCardIntent
import ru.mercury.vpclient.features.loyalty_code_sheet.intent.LoyaltyCodeIntent
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationPlaceType
import ru.mercury.vpclient.shared.data.network.type.CheckoutBonusType
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CheckoutIntent: Intent {
    data object CollectClientEntity: CheckoutIntent
    data object CollectClientAddresses: CheckoutIntent
    data object LoadClientAddresses: CheckoutIntent
    data object LoadDeliveryData: CheckoutIntent
    data object OpenAddressSelection: CheckoutIntent
    data object CloseClick: CheckoutIntent
    data object PayOnlineClick: CheckoutIntent
    data object PayAtCashDeskClick: CheckoutIntent
    data object PayWithBonusesClick: CheckoutIntent
    data object AddLoyaltyCardClick: CheckoutIntent
    data object LoyaltyAddCardPhoneConfirmClick: CheckoutIntent
    data object LoyaltyAddCardCardNumberConfirmClick: CheckoutIntent
    data object StartLoyaltyCodeResendTimerTicker: CheckoutIntent
    data object PayByCardClick: CheckoutIntent
    data object PayBySbpClick: CheckoutIntent
    data object ConfirmOrderClick: CheckoutIntent
    data object StartBonusCodeResendTimerTicker: CheckoutIntent
    data object CheckPaymentResult: CheckoutIntent
    data class LoadData(val bonusType: CheckoutBonusType = CheckoutBonusType.LOYALTY_CARD): CheckoutIntent
    data class SelectPlace(val placeType: FittingConfirmationPlaceType): CheckoutIntent
    data class SelectDeliveryDay(val dayId: String): CheckoutIntent
    data class SelectDeliveryInterval(val intervalId: String): CheckoutIntent
    data class ReceiveFittingAddressesEvent(val event: FittingAddressesEvent): CheckoutIntent
    data class OnLoyaltyAddCardIntent(val intent: LoyaltyAddCardIntent): CheckoutIntent
    data class OnLoyaltyCodeIntent(val intent: LoyaltyCodeIntent): CheckoutIntent
    data class OnCheckoutBonusIntent(val intent: CheckoutBonusIntent): CheckoutIntent
    data class OnCheckoutPaymentMethodIntent(val intent: CheckoutPaymentMethodIntent): CheckoutIntent
    data class OnCheckoutSbpBankIntent(val intent: CheckoutSbpBankIntent): CheckoutIntent
    data class OnCheckoutBankCardIntent(val intent: CheckoutBankCardIntent): CheckoutIntent
    data class OnCheckoutAmountChangedIntent(val intent: CheckoutAmountChangedIntent): CheckoutIntent
}
