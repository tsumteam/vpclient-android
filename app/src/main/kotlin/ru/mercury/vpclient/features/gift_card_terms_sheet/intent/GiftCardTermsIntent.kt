package ru.mercury.vpclient.features.gift_card_terms_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface GiftCardTermsIntent: Intent {
    data object DismissRequest: GiftCardTermsIntent
}
