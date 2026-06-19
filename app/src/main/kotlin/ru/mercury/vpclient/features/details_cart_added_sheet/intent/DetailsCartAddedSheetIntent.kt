package ru.mercury.vpclient.features.details_cart_added_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DetailsCartAddedSheetIntent: Intent {
    data object ContinueShoppingClick: DetailsCartAddedSheetIntent
    data object CartClick: DetailsCartAddedSheetIntent
    data object DismissRequest: DetailsCartAddedSheetIntent
}
