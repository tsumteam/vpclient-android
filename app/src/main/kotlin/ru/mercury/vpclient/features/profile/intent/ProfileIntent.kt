package ru.mercury.vpclient.features.profile.intent

import ru.mercury.vpclient.features.loyalty_add_card_sheet.intent.LoyaltyAddCardIntent
import ru.mercury.vpclient.features.loyalty_code_sheet.intent.LoyaltyCodeIntent
import ru.mercury.vpclient.features.profile_logout_dialog.intent.ProfileLogoutIntent
import ru.mercury.vpclient.features.profile_privileges_sheet.intent.ProfilePrivilegeIntent
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
    data object LoadActivityCounters: ProfileIntent
    data object LoadCatalogViewHistory: ProfileIntent
    data object LoadLoyaltyCardInfo: ProfileIntent
    data object LoyaltyCardQrClick: ProfileIntent
    data object LoyaltyCardMoreClick: ProfileIntent
    data object NotificationClick: ProfileIntent
    data object AddLoyaltyCardClick: ProfileIntent
    data object AlphaBankBannerCloseClick: ProfileIntent
    data object AlphaBankBannerMoreClick: ProfileIntent
    data object MyDataClick: ProfileIntent
    data object PurchasesClick: ProfileIntent
    data object InformationClick: ProfileIntent
    data object QrCodeClick: ProfileIntent
    data object FavoriteBrandsClick: ProfileIntent
    data object ViewHistoryViewMoreClick: ProfileIntent
    data object ShowLogoutDialog: ProfileIntent
    data object CartClick: ProfileIntent
    data object FittingClick: ProfileIntent
    data object MessengerClick: ProfileIntent
    data object LoyaltyAddCardPhoneConfirmClick: ProfileIntent
    data object LoyaltyAddCardCardNumberConfirmClick: ProfileIntent
    data object StartLoyaltyCodeResendTimerTicker: ProfileIntent
    data class ViewHistoryProductClick(val productId: String): ProfileIntent
    data class OnProfileLogoutIntent(val intent: ProfileLogoutIntent): ProfileIntent
    data class OnLoyaltyAddCardIntent(val intent: LoyaltyAddCardIntent): ProfileIntent
    data class OnLoyaltyCodeIntent(val intent: LoyaltyCodeIntent): ProfileIntent
    data class OnProfilePrivilegeIntent(val intent: ProfilePrivilegeIntent): ProfileIntent
}
