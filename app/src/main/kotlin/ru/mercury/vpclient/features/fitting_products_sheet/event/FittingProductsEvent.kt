package ru.mercury.vpclient.features.fitting_products_sheet.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface FittingProductsEvent: Event {
    data object DismissRequest: FittingProductsEvent
    data class ConfirmClick(val productIds: List<String>): FittingProductsEvent
}
