package ru.mercury.vpclient.features.profile_privileges_sheet.model

import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

data class ProfilePrivilegesModel(
    val cardType: LoyaltyCardType
): Model {

    val bonusTextRes: Int
        get() = when (cardType) {
            LoyaltyCardType.Black -> ClientStrings.ProfileAlphaBankPrivilegeBlackBonus
            LoyaltyCardType.Gold,
            LoyaltyCardType.Silver -> ClientStrings.ProfileAlphaBankPrivilegeBlackGoldBonus
        }

    val serviceTextRes: Int
        get() = when (cardType) {
            LoyaltyCardType.Black -> ClientStrings.ProfileAlphaBankPrivilegeBlackService
            LoyaltyCardType.Gold,
            LoyaltyCardType.Silver -> ClientStrings.ProfileAlphaBankPrivilegeBlackGoldService
        }
}
