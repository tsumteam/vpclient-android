package ru.mercury.vpclient.features.fitting_confirmation.intent

import ru.mercury.vpclient.shared.mvi.Intent
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationDeliveryMode
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationPlaceType

sealed interface FittingConfirmationIntent: Intent {
    data object BackClick: FittingConfirmationIntent
    data object ConfirmClick: FittingConfirmationIntent
    data object LoadFittingData: FittingConfirmationIntent
    data class SelectPlace(val placeType: FittingConfirmationPlaceType): FittingConfirmationIntent
    data class SelectDeliveryMode(val mode: FittingConfirmationDeliveryMode): FittingConfirmationIntent
    data class SelectSingleDay(val dayId: String): FittingConfirmationIntent
    data class SelectSingleInterval(val intervalId: String): FittingConfirmationIntent
    data class SelectDeliveryDay(val deliveryId: String, val dayId: String): FittingConfirmationIntent
    data class SelectDeliveryInterval(val deliveryId: String, val intervalId: String): FittingConfirmationIntent
    data class ChangeDeliveryTimeClick(val deliveryId: String): FittingConfirmationIntent
}
