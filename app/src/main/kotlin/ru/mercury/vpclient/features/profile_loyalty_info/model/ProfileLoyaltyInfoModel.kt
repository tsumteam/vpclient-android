package ru.mercury.vpclient.features.profile_loyalty_info.model

import ru.mercury.vpclient.shared.data.entity.LoyaltyCardDescription
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardInfo
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType
import ru.mercury.vpclient.shared.data.entity.ProfileLoyaltyInfoRowState
import ru.mercury.vpclient.shared.data.entity.ProfileLoyaltyInfoRowType
import ru.mercury.vpclient.shared.domain.mapper.order
import ru.mercury.vpclient.shared.mvi.Model

data class ProfileLoyaltyInfoModel(
    val isLoading: Boolean = true,
    val isUnlinkLoading: Boolean = false,
    val isUnlinkDialogVisible: Boolean = false,
    val cardInfo: LoyaltyCardInfo = LoyaltyCardInfo(),
    val cardTypes: List<LoyaltyCardDescription> = emptyList(),
    val selectedCardType: LoyaltyCardType = LoyaltyCardType.Black
): Model {

    val selectedCardDescription: LoyaltyCardDescription?
        get() = cardTypes.firstOrNull { cardType -> cardType.type == selectedCardType }

    val currentCardDescription: LoyaltyCardDescription?
        get() = cardTypes.firstOrNull { cardType -> cardType.type == cardInfo.typeCard }

    val sortedCardTypes: List<LoyaltyCardDescription>
        get() = cardTypes.sortedBy { cardType -> cardType.type.order }

    val bonusRules: String
        get() = currentCardDescription?.bonusRules.orEmpty()

    val showBonusRules: Boolean
        get() = bonusRules.isNotBlank()

    val infoRows: List<ProfileLoyaltyInfoRowState>
        get() = listOfNotNull(
            selectedCardDescription?.cardName
                ?.takeIf(String::isNotBlank)
                ?.let { value -> ProfileLoyaltyInfoRowState(ProfileLoyaltyInfoRowType.CardType, value) },
            selectedCardDescription?.termsForObtaining
                ?.takeIf(String::isNotBlank)
                ?.let { value -> ProfileLoyaltyInfoRowState(ProfileLoyaltyInfoRowType.TermsForObtaining, value) },
            selectedCardDescription?.validity
                ?.takeIf(String::isNotBlank)
                ?.let { value -> ProfileLoyaltyInfoRowState(ProfileLoyaltyInfoRowType.Validity, value) },
            selectedCardDescription?.renewalTerms
                ?.takeIf(String::isNotBlank)
                ?.let { value -> ProfileLoyaltyInfoRowState(ProfileLoyaltyInfoRowType.RenewalTerms, value) }
        )
}
