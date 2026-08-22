package ru.mercury.vpclient.features.details_cart_added_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DetailsCartAddedIntent: Intent {
    data object DismissClick: DetailsCartAddedIntent
    data object ContinueShoppingClick: DetailsCartAddedIntent
    data object CartClick: DetailsCartAddedIntent
}
