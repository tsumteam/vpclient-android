package ru.mercury.vpclient.features.details.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DetailsIntent: Intent {
    data object CollectProduct: DetailsIntent
    data object LoadProduct: DetailsIntent
    data object BackClick: DetailsIntent
    data object MessageClick: DetailsIntent
    data object SizeTableClick: DetailsIntent
    data object AddToBasketClick: DetailsIntent
    data object HideSizePicker: DetailsIntent
    data object ShowWearWithSheet: DetailsIntent
    data object HideWearWithSheet: DetailsIntent
    data object ShowMessageSheet: DetailsIntent
    data object HideMessageSheet: DetailsIntent
    data class SizeClick(val index: Int): DetailsIntent
    data class ColorClick(val index: Int): DetailsIntent
    data class ButtonClick(val index: Int): DetailsIntent
    data class ProductClick(val id: String): DetailsIntent
    data class OpenMediaViewer(val initialPage: Int): DetailsIntent
}
