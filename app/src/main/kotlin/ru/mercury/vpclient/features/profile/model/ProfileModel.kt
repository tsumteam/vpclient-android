package ru.mercury.vpclient.features.profile.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.model.ProfileLoyaltyAddCardMode
import ru.mercury.vpclient.features.profile_privileges_sheet.model.ProfilePrivilegesModel
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.LoyaltyCardInfoEntity
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.mvi.Model

data class ProfileModel(
    val cartCount: Int = 0,
    val cartBadge: Int = 0,
    val fittingCount: Int = 0,
    val notificationCount: Int = 0,
    val orderCount: Int = 0,
    val viewHistoryProducts: List<CatalogFilterProductsEntity> = emptyList(),
    val viewHistoryJob: Job? = null,
    val loyaltyCardInfoJob: Job? = null,
    val isLogoutDialogVisible: Boolean = false,
    val logoutJob: Job? = null,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty,
    val clientEntity: ClientEntity = ClientEntity.Empty,
    val loyaltyCardInfoEntity: LoyaltyCardInfoEntity = LoyaltyCardInfoEntity.Empty,
    val alphaBankBannerCardType: LoyaltyCardType? = null,
    val isProfilePrivilegesSheetVisible: Boolean = false,
    val isLoyaltyAddCardSheetVisible: Boolean = false,
    val loyaltyAddCardMode: ProfileLoyaltyAddCardMode = ProfileLoyaltyAddCardMode.Phone,
    val loyaltyAddCardPhone: String = "",
    val loyaltyAddCardCardNumber: String = "",
    val loyaltyAddCardJob: Job? = null,
    val isLoyaltyAddCardPhoneErrorVisible: Boolean = false,
    val isLoyaltyCodeSheetVisible: Boolean = false,
    val loyaltyCodeMode: ProfileLoyaltyAddCardMode = ProfileLoyaltyAddCardMode.Phone,
    val loyaltyCodePhone: String = "",
    val loyaltyCodeCardNumber: String = "",
    val loyaltyCode: String = "",
    val isLoyaltyCodeLoading: Boolean = false,
    val isLoyaltyCodeResendLoading: Boolean = false,
    val isLoyaltyCodeErrorVisible: Boolean = false,
    val loyaltyCodeResendTimerStartedAt: Long = System.currentTimeMillis(),
    val loyaltyCodeResendSecondsLeft: Int = 0,
    val loyaltyCodeResendTimerJob: Job? = null
): Model {

    val isLogoutLoading: Boolean
        get() = logoutJob?.isActive == true

    val isViewHistoryLoading: Boolean
        get() = viewHistoryJob?.isActive == true

    val isLoyaltyCardLoading: Boolean
        get() = loyaltyCardInfoJob?.isActive == true

    val cartText: String
        get() = when {
            cartCount > 0 -> cartCount.toString()
            else -> ""
        }

    val isCartBadgeVisible: Boolean
        get() = cartBadge > 0

    val visibleViewHistoryProducts: List<CatalogFilterProductsEntity>
        get() = viewHistoryProducts.take(10)

    val visibleLoyaltyCardInfo: LoyaltyCardInfoEntity
        get() = loyaltyCardInfoEntity

    val visibleAlphaBankBannerCardType: LoyaltyCardType
        get() = alphaBankBannerCardType ?: LoyaltyCardType.Black

    val isLoyaltyCardVisible: Boolean
        get() = loyaltyCardInfoEntity.loyaltyCardNumber.isNotBlank()

    val isAddLoyaltyCardVisible: Boolean
        get() = loyaltyCardInfoEntity.loyaltyCardNumber.isBlank() && !isLoyaltyCardLoading

    val isLoyaltyCardPlaceholderVisible: Boolean
        get() = loyaltyCardInfoEntity.loyaltyCardNumber.isBlank() && isLoyaltyCardLoading

    val isAlphaBankBannerVisible: Boolean
        get() = alphaBankBannerCardType != null

    val isViewHistoryVisible: Boolean
        get() = isViewHistoryLoading || viewHistoryProducts.isNotEmpty()

    val isViewHistoryMoreVisible: Boolean
        get() = viewHistoryProducts.size > 10

    val fittingText: String
        get() = if (fittingCount > 0) fittingCount.toString() else ""

    val isFittingButtonVisible: Boolean
        get() = fittingCount > 0

    val isFittingBadgeVisible: Boolean
        get() = activeEmployee.hasFittingBadge

    val isMessengerBadgeVisible: Boolean
        get() = activeEmployee.hasMessengerBadge

    val isNotificationBadgeVisible: Boolean
        get() = notificationCount > 0

    val isPurchasesBadgeVisible: Boolean
        get() = orderCount > 0

    val profilePrivilegesModel: ProfilePrivilegesModel
        get() = ProfilePrivilegesModel(cardType = visibleAlphaBankBannerCardType)
}
