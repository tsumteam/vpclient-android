package ru.mercury.vpclient.features.main.tabs.home.stack.routes.intent

import ru.mercury.vpclient.core.BoutiqueId
import ru.mercury.vpclient.core.DeliveryId
import ru.mercury.vpclient.core.mvi.Intent

sealed interface RoutesIntent: Intent {
    data object CollectRouteData: RoutesIntent
    data object RequestRoutes: RoutesIntent
    data object CloseTripButtonClick: RoutesIntent
    data object SectionBoutiquesClick: RoutesIntent
    data object SectionDeliveriesClick: RoutesIntent
    data object SectionReturnsClick: RoutesIntent
    data object CallTel: RoutesIntent
    data object CallWhatsApp: RoutesIntent
    data object WriteWhatsApp: RoutesIntent
    data object PullToRefresh: RoutesIntent
    data object LoadDetailsProducts: RoutesIntent
    data class BoutiqueClick(val boutiqueId: BoutiqueId): RoutesIntent
    data class DeliveryClick(val deliveryId: DeliveryId): RoutesIntent
    data class ReturnClick(val boutiqueId: BoutiqueId): RoutesIntent
    data class CopyDeliveryClick(val deliveryId: DeliveryId): RoutesIntent
    data class SetPhone(val phone: String): RoutesIntent
}
