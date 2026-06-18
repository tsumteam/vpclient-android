package ru.mercury.vpclient.features.size_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface SizeSheetIntent: Intent {
    data object SizeTableClick: SizeSheetIntent
    data object AddToBasketClick: SizeSheetIntent
    data object DismissRequest: SizeSheetIntent
    data class SizeClick(val index: Int): SizeSheetIntent
}
