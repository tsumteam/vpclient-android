package ru.mercury.vpclient.features.profile.intent

import ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.model.ProfileLoyaltyAddCardMode
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileIntent: Intent {
    data object CollectCartCount: ProfileIntent
    data object CollectFittingCount: ProfileIntent
    data object CollectClientEntity: ProfileIntent
    data object CollectActiveEmployee: ProfileIntent
    data object CollectNotificationCount: ProfileIntent
    data object CollectOrderCount: ProfileIntent
    data object CollectLoyaltyCardInfoEntity: ProfileIntent
    data object CollectViewHistoryProducts: ProfileIntent
    data object LoadCartData: ProfileIntent
    data object LoadActivityCounters: ProfileIntent
    data object LoadCatalogViewHistory: ProfileIntent
    data object LoadLoyaltyCardInfo: ProfileIntent
    data object LoyaltyCardQrClick: ProfileIntent
    data object LoyaltyCardMoreClick: ProfileIntent
    data object NotificationClick: ProfileIntent
    data object AddLoyaltyCardClick: ProfileIntent
    data object AlphaBankBannerCloseClick: ProfileIntent
    data object AlphaBankBannerMoreClick: ProfileIntent
    data object DismissAlphaBankPrivilegesSheet: ProfileIntent
    data object MyDataClick: ProfileIntent
    data object PurchasesClick: ProfileIntent
    data object InformationClick: ProfileIntent
    data object QrCodeClick: ProfileIntent
    data object FavoriteBrandsClick: ProfileIntent
    data object ViewHistoryViewMoreClick: ProfileIntent
    data object ShowLogoutDialog: ProfileIntent
    data object DismissLogoutDialog: ProfileIntent
    data object Logout: ProfileIntent
    data object CartClick: ProfileIntent
    data object FittingClick: ProfileIntent
    data object MessengerClick: ProfileIntent
    data object DismissLoyaltyAddCardSheet: ProfileIntent
    data object LoyaltyAddCardPhoneConfirmClick: ProfileIntent
    data object LoyaltyAddCardCardNumberConfirmClick: ProfileIntent
    data object DismissLoyaltyCodeSheet: ProfileIntent
    data object StartLoyaltyCodeResendTimerTicker: ProfileIntent
    data object LoyaltyCodeConfirmClick: ProfileIntent
    data object LoyaltyCodeResendCodeClick: ProfileIntent
    data class ViewHistoryProductClick(val productId: String): ProfileIntent
    data class LoyaltyAddCardModeClick(val mode: ProfileLoyaltyAddCardMode): ProfileIntent
    data class LoyaltyAddCardPhoneChange(val phone: String): ProfileIntent
    data class LoyaltyAddCardCardNumberChange(val cardNumber: String): ProfileIntent
    data class LoyaltyCodeChange(val code: String): ProfileIntent
}
