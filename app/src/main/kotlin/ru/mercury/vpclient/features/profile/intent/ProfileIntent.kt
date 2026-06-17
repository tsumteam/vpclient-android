package ru.mercury.vpclient.features.profile.intent

import ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.intent.ProfileLoyaltyAddCardIntent
import ru.mercury.vpclient.features.profile_loyalty_code_sheet.intent.ProfileLoyaltyCodeIntent
import ru.mercury.vpclient.features.profile_privileges_sheet.intent.ProfilePrivilegeIntent
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileIntent: Intent {
    data object CollectCartSize: ProfileIntent
    data object CollectClientEntity: ProfileIntent
    data object CollectActiveEmployee: ProfileIntent
    data object CollectViewHistoryProducts: ProfileIntent
    data object LoadEmployees: ProfileIntent
    data object LoadCartData: ProfileIntent
    data object LoadViewHistoryProducts: ProfileIntent
    data object LoadLoyaltyCardInfo: ProfileIntent
    data object NotificationClick: ProfileIntent
    data object AddLoyaltyCardClick: ProfileIntent
    data object LoyaltyCardQrClick: ProfileIntent
    data object LoyaltyCardMoreClick: ProfileIntent
    data object AlphaBankBannerCloseClick: ProfileIntent
    data object AlphaBankBannerMoreClick: ProfileIntent
    data object DismissAlphaBankPrivilegesSheet: ProfileIntent
    data object MyDataClick: ProfileIntent
    data object PurchasesClick: ProfileIntent
    data object InformationClick: ProfileIntent
    data object QrCodeClick: ProfileIntent
    data object ViewHistoryViewMoreClick: ProfileIntent
    data object ShowLogoutDialog: ProfileIntent
    data object DismissLogoutDialog: ProfileIntent
    data object Logout: ProfileIntent
    data object CartClick: ProfileIntent
    data object FittingClick: ProfileIntent
    data object MessengerClick: ProfileIntent
    data class ViewHistoryProductClick(val productId: String): ProfileIntent
    data class LoyaltyAddCardSheetIntent(val intent: ProfileLoyaltyAddCardIntent): ProfileIntent
    data class LoyaltyCodeSheetIntent(val intent: ProfileLoyaltyCodeIntent): ProfileIntent
    data class ProfilePrivilegesSheetIntent(val intent: ProfilePrivilegeIntent): ProfileIntent
}
