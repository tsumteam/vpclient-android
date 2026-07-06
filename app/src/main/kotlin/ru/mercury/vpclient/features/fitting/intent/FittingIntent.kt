package ru.mercury.vpclient.features.fitting.intent

import ru.mercury.vpclient.shared.data.entity.CartPayMode
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingIntent: Intent {
    data object CollectCartCount: FittingIntent
    data object CollectFittingCount: FittingIntent
    data object CollectCartProducts: FittingIntent
    data object CollectActiveEmployee: FittingIntent
    data object CollectCompilationEntities: FittingIntent
    data object LoadCompilationsClient: FittingIntent
    data object SearchClick: FittingIntent
    data object CartClick: FittingIntent
    data object FittingClick: FittingIntent
    data object MessengerClick: FittingIntent
    data object BuyClick: FittingIntent
    data object FittingDeliveryClick: FittingIntent
    data class ProductClick(val id: String): FittingIntent
    data class CompilationClick(val id: Int): FittingIntent
    data class ChangePaySwitch(val product: CartProduct, val paySwitch: Boolean): FittingIntent
    data class SelectPayMode(val mode: CartPayMode): FittingIntent
}
