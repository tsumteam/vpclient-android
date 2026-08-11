package ru.mercury.vpclient.features.checkout.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.features.loyalty_add_card_sheet.model.LoyaltyAddCardMode
import ru.mercury.vpclient.shared.data.entity.CheckoutSbpBank
import ru.mercury.vpclient.shared.data.entity.FittingCheckoutData
import ru.mercury.vpclient.shared.data.entity.FittingCheckoutOnlinePaymentMethod
import ru.mercury.vpclient.shared.data.entity.FittingCheckoutPaymentCardModel
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationData
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationPlaceType
import ru.mercury.vpclient.shared.data.network.type.PaymentType
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.shared.domain.mapper.rubles
import ru.mercury.vpclient.shared.mvi.Model

data class CheckoutModel(
    val source: CheckoutSource = CheckoutSource.Fitting,
    val fittingCheckoutData: FittingCheckoutData = FittingCheckoutData.Empty,
    val deliveryData: FittingConfirmationData = FittingConfirmationData(),
    val selectedPlaceType: FittingConfirmationPlaceType = FittingConfirmationPlaceType.Boutique,
    val clientAddresses: List<ClientDeliveryAddressEntity> = emptyList(),
    val selectedClientAddressId: Int? = null,
    val selectedDayId: String? = null,
    val selectedIntervalId: String? = null,
    val deliveryLoadJob: Job? = null,
    val addressListJob: Job? = null,
    val isAmountChangedDialogVisible: Boolean = false,
    val amountChangedMessage: String = "",
    val clientEntity: ClientEntity = ClientEntity.Empty,
    val paymentType: PaymentType = PaymentType.ONLINE_PAYMENT_CLIENT,
    val isPayWithBonuses: Boolean = true,
    val loadDataJob: Job? = null,
    val isLoyaltyAddCardSheetVisible: Boolean = false,
    val loyaltyAddCardMode: LoyaltyAddCardMode = LoyaltyAddCardMode.Phone,
    val loyaltyAddCardPhone: String = "",
    val loyaltyAddCardCardNumber: String = "",
    val loyaltyAddCardJob: Job? = null,
    val isLoyaltyAddCardPhoneErrorVisible: Boolean = false,
    val isLoyaltyCodeSheetVisible: Boolean = false,
    val loyaltyCodeMode: LoyaltyAddCardMode = LoyaltyAddCardMode.Phone,
    val loyaltyCodePhone: String = "",
    val loyaltyCodeCardNumber: String = "",
    val loyaltyCode: String = "",
    val loyaltyCodeConfirmJob: Job? = null,
    val loyaltyCodeResendJob: Job? = null,
    val isLoyaltyCodeErrorVisible: Boolean = false,
    val loyaltyCodeResendTimerStartedAt: Long = System.currentTimeMillis(),
    val loyaltyCodeResendSecondsLeft: Int = 0,
    val loyaltyCodeResendTimerJob: Job? = null,
    val paymentOrderNumber: String = "",
    val pendingOnlinePaymentMethod: FittingCheckoutOnlinePaymentMethod = FittingCheckoutOnlinePaymentMethod.Card,
    val paymentJob: Job? = null,
    val isBonusConfirmationSheetVisible: Boolean = false,
    val bonusCode: String = "",
    val bonusConfirmJob: Job? = null,
    val bonusResendJob: Job? = null,
    val isBonusCodeErrorVisible: Boolean = false,
    val bonusCodeResendTimerStartedAt: Long = System.currentTimeMillis(),
    val bonusCodeResendSecondsLeft: Int = 0,
    val bonusCodeResendTimerJob: Job? = null,
    val isPaymentMethodSheetVisible: Boolean = false,
    val paymentCards: List<FittingCheckoutPaymentCardModel> = emptyList(),
    val isBankCardSheetVisible: Boolean = false,
    val bankCardNumber: String = "",
    val bankCardExpirationDate: String = "",
    val bankCardCvv: String = "",
    val isBankCardSaveChecked: Boolean = false,
    val isBankCardNumberErrorVisible: Boolean = false,
    val isBankCardExpirationDateErrorVisible: Boolean = false,
    val paymentResultCheckJob: Job? = null,
    val isPaymentExternalFlowStarted: Boolean = false,
    val isSbpBankSheetVisible: Boolean = false,
    val sbpPaymentUrl: String = "",
    val sbpBanks: List<CheckoutSbpBank> = emptyList()
): Model {

    val isCartCheckout: Boolean
        get() = source == CheckoutSource.Cart

    val isDeliveryPlaceSectionVisible: Boolean
        get() = isCartCheckout

    val isLoading: Boolean
        get() = loadDataJob?.isActive == true

    val isLoyaltyCodeLoading: Boolean
        get() = loyaltyCodeConfirmJob?.isActive == true

    val isLoyaltyCodeResendLoading: Boolean
        get() = loyaltyCodeResendJob?.isActive == true

    val isPaymentLoading: Boolean
        get() = paymentJob?.isActive == true

    val isBonusCodeLoading: Boolean
        get() = bonusConfirmJob?.isActive == true

    val isBonusCodeResendLoading: Boolean
        get() = bonusResendJob?.isActive == true

    val isLoyaltyCardLinked: Boolean
        get() = !fittingCheckoutData.loyaltyCardNumber.isNullOrBlank()

    val isToolbarBonusVisible: Boolean
        get() = isLoyaltyCardLinked && fittingCheckoutData.totalAvailableBonusAmount > 0

    val isPayWithBonusesVisible: Boolean
        get() = when {
            isCartCheckout -> isLoyaltyCardLinked && isOnlinePaymentSelected
            else -> isLoyaltyCardLinked && fittingCheckoutData.availableBonusAmount > 0
        }

    val isPayWithBonusesEnabled: Boolean
        get() = fittingCheckoutData.availableBonusAmount > 0

    val isAddLoyaltyCardVisible: Boolean
        get() = !isLoyaltyCardLinked && source != CheckoutSource.ExistingOrder

    val isPromotionDiscountRowVisible: Boolean
        get() = fittingCheckoutData.promotionDiscount > 0

    val isBonusAmountVisible: Boolean
        get() = isPayWithBonusesVisible && isPayWithBonusesEnabled && isPayWithBonuses

    val isBonusAmountRowVisible: Boolean
        get() = isBonusAmountVisible

    val isOnlinePaymentSelected: Boolean
        get() = paymentType == PaymentType.ONLINE_PAYMENT_CLIENT

    val selectedPaymentTypeIndex: Int
        get() = when (paymentType) {
            PaymentType.CASHLESS_COURIER -> 1
            else -> 0
        }

    val selectedClientAddress: ClientDeliveryAddressEntity?
        get() = clientAddresses.firstOrNull { address -> address.id == selectedClientAddressId }

    val selectedDeliveryInterval: FittingConfirmationDeliveryInterval?
        get() = deliveryData.singleIntervals.firstOrNull { interval -> interval.id == selectedIntervalId }

    val isDeliveryLoading: Boolean
        get() = deliveryLoadJob?.isActive == true

    val isDeliverySelectionValid: Boolean
        get() = !isCartCheckout || (
            selectedIntervalId != null &&
                when (selectedPlaceType) {
                    FittingConfirmationPlaceType.Boutique -> deliveryData.boutiqueAddress != null
                    FittingConfirmationPlaceType.Home -> selectedClientAddress != null
                    FittingConfirmationPlaceType.Other -> false
                }
            )

    val hasPayableItems: Boolean
        get() = when (source) {
            CheckoutSource.Cart -> fittingCheckoutData.itemCount > 0
            CheckoutSource.Fitting -> fittingCheckoutData.deliveryIds.isNotEmpty()
            CheckoutSource.ExistingOrder -> fittingCheckoutData.itemCount > 0
        }

    val isPaymentEnabled: Boolean
        get() = hasPayableItems && !isLoading && !isPaymentLoading && isDeliverySelectionValid

    val isPayByCardEnabled: Boolean
        get() = hasPayableItems && !isLoading && isDeliverySelectionValid

    val isPaySbpEnabled: Boolean
        get() = hasPayableItems && !isLoading && isDeliverySelectionValid &&
            (!isCartCheckout || totalAmountValue <= SBP_PAYMENT_LIMIT)

    val totalAvailableBonusAmountText: String
        get() = fittingCheckoutData.totalAvailableBonusAmount.rubles

    val orderAmountText: String
        get() = fittingCheckoutData.orderAmount.rubles

    val promotionDiscountText: String
        get() = "-${fittingCheckoutData.promotionDiscount.rubles}"

    val bonusAmountText: String
        get() = "-${fittingCheckoutData.availableBonusAmount.rubles}"

    val totalAmountValue: Int
        get() {
            val totalBeforeBonuses = fittingCheckoutData.orderAmount - fittingCheckoutData.promotionDiscount
            val bonusAmount = when {
                isBonusAmountVisible -> fittingCheckoutData.availableBonusAmount
                else -> 0
            }
            return (totalBeforeBonuses - bonusAmount).coerceAtLeast(0)
        }

    val totalAmountText: String
        get() = totalAmountValue.rubles

    private companion object {
        private const val SBP_PAYMENT_LIMIT = 1_000_000
    }
}
