package ru.mercury.vpclient.features.profile.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.model.ProfileLoyaltyAddCardModel
import ru.mercury.vpclient.features.profile_loyalty_code_sheet.model.ProfileLoyaltyCodeModel
import ru.mercury.vpclient.features.profile_privileges_sheet.model.ProfilePrivilegesModel
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardInfo
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.mapper.fittingText
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasFittingProducts
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.mvi.Model

data class ProfileModel(
    val logoutJob: Job? = null,
    val cartSize: Int = 0,
    val cartBadge: Int = 0,
    val viewHistoryProducts: List<CatalogFilterProductsEntity> = emptyList(),
    val isViewHistoryLoading: Boolean = true,
    val isLoyaltyCardLoading: Boolean = true,
    val isLogoutDialogVisible: Boolean = false,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty,
    val clientEntity: ClientEntity = ClientEntity.Empty,
    val loyaltyCardInfo: LoyaltyCardInfo? = null,
    val alphaBankBannerCardType: LoyaltyCardType? = null,
    val profilePrivilegesSheet: ProfilePrivilegesModel? = null,
    val loyaltyAddCardSheet: ProfileLoyaltyAddCardModel? = null,
    val loyaltyCodeSheet: ProfileLoyaltyCodeModel? = null
): Model {

    val isLogoutLoading: Boolean
        get() = logoutJob?.isActive == true

    val cartText: String
        get() = when {
            cartSize > 0 -> cartSize.toString()
            else -> ""
        }

    val showCartBadge: Boolean
        get() = cartBadge > 0

    val visibleViewHistoryProducts: List<CatalogFilterProductsEntity>
        get() = viewHistoryProducts.take(10)

    val showViewHistory: Boolean
        get() = isViewHistoryLoading || viewHistoryProducts.isNotEmpty()

    val showViewHistoryMore: Boolean
        get() = viewHistoryProducts.size > 10

    val fittingText: String
        get() = activeEmployee.fittingText

    val showFittingButton: Boolean
        get() = activeEmployee.hasFittingProducts

    val showFittingBadge: Boolean
        get() = activeEmployee.hasFittingBadge

    val showMessengerBadge: Boolean
        get() = activeEmployee.hasMessengerBadge
}
