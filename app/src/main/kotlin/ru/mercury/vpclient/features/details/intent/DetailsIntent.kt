package ru.mercury.vpclient.features.details.intent

import ru.mercury.vpclient.features.details_cart_added_sheet.intent.DetailsCartAddedIntent
import ru.mercury.vpclient.features.details_message_sheet.intent.DetailsMessageIntent
import ru.mercury.vpclient.features.details_wear_with_sheet.intent.DetailsWearWithIntent
import ru.mercury.vpclient.shared.data.entity.DetailsField
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DetailsIntent: Intent {
    data object CollectProduct: DetailsIntent
    data object CollectCartProducts: DetailsIntent
    data object CollectCartCount: DetailsIntent
    data object CollectFittingCount: DetailsIntent
    data object CollectActiveEmployee: DetailsIntent
    data object LoadCartData: DetailsIntent
    data object LoadProduct: DetailsIntent
    data object BackClick: DetailsIntent
    data object CartClick: DetailsIntent
    data object FittingClick: DetailsIntent
    data object MessengerClick: DetailsIntent
    data object MessageClick: DetailsIntent
    data object SizeTableClick: DetailsIntent
    data object AddToBasketClick: DetailsIntent
    data object OpenVideo: DetailsIntent
    data object BrandClick: DetailsIntent
    data object ShowWearWithSheet: DetailsIntent
    data object ShowMessageSheet: DetailsIntent
    data class SizeClick(val index: Int): DetailsIntent
    data class ColorClick(val index: Int): DetailsIntent
    data class ButtonClick(val index: Int): DetailsIntent
    data class ProductClick(val id: String): DetailsIntent
    data class ProductBasketClick(val product: CatalogFilterProductsEntity): DetailsIntent
    data class OpenMedia(val initialPage: Int): DetailsIntent
    data class FieldCopyClick(val field: DetailsField): DetailsIntent
    data class OnDetailsMessageIntent(val intent: DetailsMessageIntent): DetailsIntent
    data class OnDetailsCartAddedIntent(val intent: DetailsCartAddedIntent): DetailsIntent
    data class OnDetailsWearWithIntent(val intent: DetailsWearWithIntent): DetailsIntent
    data class OnSizeSheetIntent(val intent: ru.mercury.vpclient.features.size_sheet.intent.SizeSheetIntent): DetailsIntent
}
