package ru.mercury.vpclient.features.filter_price_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FilterPriceIntent: Intent {
    data object HideFilterPriceDialog: FilterPriceIntent
    data object ResetPrice: FilterPriceIntent
    data object ConfirmPrice: FilterPriceIntent
    data class ChangeMinPrice(val value: String): FilterPriceIntent
    data class ChangeMaxPrice(val value: String): FilterPriceIntent
    data class SelectPricePreset(val valueId: String): FilterPriceIntent
}
