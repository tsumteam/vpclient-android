package ru.mercury.vpclient.features.profile_view_history.intent

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileViewHistoryIntent: Intent {
    data object CollectCartCount: ProfileViewHistoryIntent
    data object CollectCartProducts: ProfileViewHistoryIntent
    data object LoadCartData: ProfileViewHistoryIntent
    data object BackClick: ProfileViewHistoryIntent
    data object SearchClick: ProfileViewHistoryIntent
    data object CartClick: ProfileViewHistoryIntent
    data class ProductClick(val id: String): ProfileViewHistoryIntent
    data class ProductBasketClick(val product: CatalogFilterProductsEntity): ProfileViewHistoryIntent
}
