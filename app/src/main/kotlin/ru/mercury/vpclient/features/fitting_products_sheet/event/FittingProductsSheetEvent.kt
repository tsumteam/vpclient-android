package ru.mercury.vpclient.features.fitting_products_sheet.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface FittingProductsSheetEvent: Event {
    data object DismissRequest: FittingProductsSheetEvent
    data class ConfirmClick(val productIds: List<String>): FittingProductsSheetEvent
}
