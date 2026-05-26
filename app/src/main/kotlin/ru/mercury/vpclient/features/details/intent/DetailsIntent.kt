package ru.mercury.vpclient.features.details.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DetailsIntent: Intent {
    data object CollectProduct: DetailsIntent
    data object CollectCartProducts: DetailsIntent
    data object CollectCartSize: DetailsIntent
    data object CollectActiveEmployee: DetailsIntent
    data object LoadEmployees: DetailsIntent
    data object LoadCartData: DetailsIntent
    data object LoadProduct: DetailsIntent
    data object BackClick: DetailsIntent
    data object CartClick: DetailsIntent
    data object FittingClick: DetailsIntent
    data object MessengerClick: DetailsIntent
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
    data class ProductBasketClick(val id: String): DetailsIntent
    data class OpenMediaViewer(val initialPage: Int): DetailsIntent
}
