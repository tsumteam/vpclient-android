package ru.mercury.vpclient.features.checkout.intent

import ru.mercury.vpclient.features.fitting_addresses.event.FittingAddressesEvent
import ru.mercury.vpclient.shared.data.entity.CheckoutSbpBank
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationPlaceType
import ru.mercury.vpclient.shared.data.entity.LoyaltyAddCardMode
import ru.mercury.vpclient.shared.data.network.type.CheckoutBonusType
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CheckoutIntent: Intent {
    data object CollectClientEntity: CheckoutIntent
    data object CollectClientAddresses: CheckoutIntent
    data object LoadClientAddresses: CheckoutIntent
    data object LoadDeliveryData: CheckoutIntent
    data object OpenAddressSelection: CheckoutIntent
    data object AmountChangedContinueClick: CheckoutIntent
    data object CloseClick: CheckoutIntent
    data object PayOnlineClick: CheckoutIntent
    data object PayAtCashDeskClick: CheckoutIntent
    data object PayWithBonusesClick: CheckoutIntent
    data object AddLoyaltyCardClick: CheckoutIntent
    data object DismissLoyaltyAddCardSheet: CheckoutIntent
    data object LoyaltyAddCardPhoneConfirmClick: CheckoutIntent
    data object LoyaltyAddCardCardNumberConfirmClick: CheckoutIntent
    data object DismissLoyaltyCodeSheet: CheckoutIntent
    data object StartLoyaltyCodeResendTimerTicker: CheckoutIntent
    data object LoyaltyCodeConfirmClick: CheckoutIntent
    data object LoyaltyCodeResendCodeClick: CheckoutIntent
    data object PayByCardClick: CheckoutIntent
    data object PayBySbpClick: CheckoutIntent
    data object ConfirmOrderClick: CheckoutIntent
    data object DismissCheckoutBonusSheet: CheckoutIntent
    data object StartBonusCodeResendTimerTicker: CheckoutIntent
    data object BonusCodeConfirmClick: CheckoutIntent
    data object BonusCodeResendClick: CheckoutIntent
    data object DismissCheckoutPaymentMethodSheet: CheckoutIntent
    data object AddNewPaymentCardClick: CheckoutIntent
    data object PaymentMethodPayClick: CheckoutIntent
    data object DismissCheckoutBankCardSheet: CheckoutIntent
    data object BankCardSaveClick: CheckoutIntent
    data object BankCardPayClick: CheckoutIntent
    data object BankCardNumberFocusLost: CheckoutIntent
    data object BankCardExpirationDateFocusLost: CheckoutIntent
    data object BankCardCvvFocusLost: CheckoutIntent
    data object CheckPaymentResult: CheckoutIntent
    data object DismissCheckoutSbpBankSheet: CheckoutIntent
    data class LoadData(val bonusType: CheckoutBonusType = CheckoutBonusType.LOYALTY_CARD): CheckoutIntent
    data class LoyaltyAddCardModeClick(val mode: LoyaltyAddCardMode): CheckoutIntent
    data class LoyaltyAddCardPhoneChange(val phone: String): CheckoutIntent
    data class LoyaltyAddCardCardNumberChange(val cardNumber: String): CheckoutIntent
    data class LoyaltyCodeChange(val code: String): CheckoutIntent
    data class BonusCodeChange(val code: String): CheckoutIntent
    data class PaymentCardClick(val cardId: String): CheckoutIntent
    data class DeletePaymentCardClick(val cardId: String): CheckoutIntent
    data class BankCardNumberChange(val value: String): CheckoutIntent
    data class BankCardExpirationDateChange(val value: String): CheckoutIntent
    data class BankCardCvvChange(val value: String): CheckoutIntent
    data class SelectPlace(val placeType: FittingConfirmationPlaceType): CheckoutIntent
    data class SelectDeliveryDay(val dayId: String): CheckoutIntent
    data class SelectDeliveryInterval(val intervalId: String): CheckoutIntent
    data class ReceiveFittingAddressesEvent(val event: FittingAddressesEvent): CheckoutIntent
    data class SbpBankClick(val bank: CheckoutSbpBank): CheckoutIntent
}
