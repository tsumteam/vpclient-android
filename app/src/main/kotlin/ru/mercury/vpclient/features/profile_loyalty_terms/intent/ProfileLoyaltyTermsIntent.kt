package ru.mercury.vpclient.features.profile_loyalty_terms.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileLoyaltyTermsIntent: Intent {
    data object LoadUrl: ProfileLoyaltyTermsIntent
    data object BackClick: ProfileLoyaltyTermsIntent
}
